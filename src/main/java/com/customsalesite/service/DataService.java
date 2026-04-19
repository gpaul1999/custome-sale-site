package com.customsalesite.service;

import com.customsalesite.dto.*;
import com.customsalesite.entity.*;
import com.customsalesite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;

    public List<ProductTypeResponse> getAllProductTypes() {
        return productTypeRepository.findByEnabled(true).stream()
                .map(this::toProductTypeResponse)
                .collect(Collectors.toList());
    }

    public ProductTypeResponse getProductTypeById(Long id) {
        ProductType pt = productTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductType not found: " + id));
        return toProductTypeResponse(pt);
    }

    public List<BrandResponse> getAllBrands() {
        return brandRepository.findByEnabled(true).stream()
                .map(this::toBrandResponse)
                .collect(Collectors.toList());
    }

    public BrandResponse getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found: " + id));
        return toBrandResponse(brand);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findByEnabled(true).stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
        return toProductResponse(product);
    }

    public List<ProductResponse> getProductsByType(Long productTypeId) {
        // Get all categories for this type, then get all products from those categories
        return productCategoryRepository.findByProductTypeIdAndEnabled(productTypeId, true).stream()
                .flatMap(category -> productRepository.findByProductCategoryIdAndEnabled(category.getId(), true).stream())
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public List<BrandResponse> getBrandsByProductType(Long productTypeId) {
        return brandRepository.findByProductTypeId(productTypeId).stream()
                .map(this::toBrandResponse)
                .collect(Collectors.toList());
    }

    public List<ProductCategoryResponse> getProductCategoriesByType(Long productTypeId) {
        return productCategoryRepository.findByProductTypeIdAndEnabled(productTypeId, true).stream()
                .map(this::toProductCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByCategory(Long productCategoryId) {
        return productRepository.findByProductCategoryIdAndEnabled(productCategoryId, true).stream()
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> filterProducts(
            Long productTypeId,
            String keyword,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean saleOnly,
            Long brandId,
            String sortBy
    ) {
        Sort sort = switch (sortBy == null ? "" : sortBy) {
            case "price_asc"  -> Sort.by("price").ascending();
            case "price_desc" -> Sort.by("price").descending();
            case "name_asc"   -> Sort.by("syntax").ascending();
            default           -> Sort.by("id").ascending();
        };
        return productRepository.findAll(
                ProductSpecification.filter(productTypeId, keyword, minPrice, maxPrice, saleOnly, brandId, true),
                sort
        ).stream().map(this::toProductResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> getSaleOffProducts() {
        return productRepository.findByEnabled(true).stream()
                .filter(p -> p.isSaleOff())
                .map(this::toProductResponse)
                .collect(Collectors.toList());
    }

    public ProductDetailResponse getProductDetailById(Long id) {
        ProductDetail detail = productDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductDetail not found: " + id));
        return toProductDetailResponse(detail);
    }

    public ProductPageData getProductPageData(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        if (!product.isEnabled()) {
            throw new RuntimeException("Product is not available: " + productId);
        }
        ProductResponse productResponse = toProductResponse(product);
        ProductDetailResponse detailResponse = product.getProductDetail() != null
                ? toProductDetailResponse(product.getProductDetail())
                : null;
        return new ProductPageData(productResponse, detailResponse);
    }

    public List<ProductResponse> searchProductsDropdown(String q, int limit) {
        if (q == null || q.trim().isEmpty()) return List.of();
        return productRepository.searchByKeyword(q.trim(), PageRequest.of(0, limit, Sort.by("id").ascending()))
                .stream().map(this::toProductResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> searchProducts(String q) {
        if (q == null || q.trim().isEmpty()) return List.of();
        return productRepository.searchByKeyword(q.trim(), PageRequest.of(0, 100, Sort.by("id").ascending()))
                .stream().map(this::toProductResponse).collect(Collectors.toList());
    }

    // Score + sort search results like dropdown (for /services search page)
    public List<ProductResponse> searchProductsWithScore(String q) {
        if (q == null || q.trim().isEmpty()) return List.of();

        List<Product> results = productRepository.searchByKeyword(q.trim(), PageRequest.of(0, 1000, Sort.by("id").ascending()))
                .stream().collect(Collectors.toList());

        // Score each product
        results.sort((a, b) -> scoreProduct(b, q) - scoreProduct(a, q));

        return results.stream().map(this::toProductResponse).collect(Collectors.toList());
    }

    private int scoreProduct(Product p, String q) {
        int score = 0;
        String ql = q.toLowerCase();

        if (p.getSyntax() != null) {
            String t = p.getSyntax().toLowerCase();
            if (t.equals(ql)) score += 100;
            else if (t.startsWith(ql)) score += 60;
            else if (t.contains(ql)) score += 30;
        }

        if (p.getDescription() != null) {
            String d = p.getDescription().toLowerCase();
            if (d.contains(ql)) score += 10;
        }

        if (p.isSaleOff()) score += 5;

        return score;
    }

    public record ProductPageData(ProductResponse product, ProductDetailResponse detail) {}

    private ProductTypeResponse toProductTypeResponse(ProductType pt) {
        return new ProductTypeResponse(pt.getId(), pt.getSyntax(), pt.getDescription());
    }

    private ProductCategoryResponse toProductCategoryResponse(com.customsalesite.entity.ProductCategory pc) {
        return new ProductCategoryResponse(
                pc.getId(),
                pc.getSyntax(),
                pc.getDescription(),
                pc.getProductType() != null ? pc.getProductType().getId() : null
        );
    }

    private BrandResponse toBrandResponse(Brand brand) {
        return new BrandResponse(
                brand.getId(),
                brand.getName(),
                brand.getLogo(),
                brand.getLongDescription()
        );
    }

    private ProductResponse toProductResponse(Product product) {
        BigDecimal salePrice = null;
        if (product.isSaleOff()
                && product.getPrice() != null
                && product.getSalePercent() != null
                && product.getSalePercent() > 0) {
            BigDecimal discount = product.getPrice()
                    .multiply(BigDecimal.valueOf(product.getSalePercent()))
                    .divide(BigDecimal.valueOf(100));
            salePrice = product.getPrice().subtract(discount);
        }

        Long typeId = null;
        String typeName = null;
        if (product.getProductCategory() != null && product.getProductCategory().getProductType() != null) {
            typeId = product.getProductCategory().getProductType().getId();
            typeName = product.getProductCategory().getProductType().getSyntax();
        }

        return new ProductResponse(
                product.getId(),
                product.getSyntax(),
                product.getDescription(),
                product.getPrice(),
                product.isSaleOff(),
                product.getSalePercent(),
                salePrice,
                product.getImages(),
                typeId,
                typeName,
                product.getProductCategory() != null ? product.getProductCategory().getId() : null,
                product.getProductCategory() != null ? product.getProductCategory().getSyntax() : null
        );
    }

    private ProductDetailResponse toProductDetailResponse(ProductDetail detail) {
        LocalDate today = LocalDate.now();
        List<PromotionResponse> promotions = detail.getPromotions() != null
                ? detail.getPromotions().stream()
                    .filter(p -> p.isEnabled())   // chỉ show promotion đang bật
                    .map(p -> new PromotionResponse(
                            p.getId(),
                            p.getTitle(),
                            p.getDescription(),
                            p.getStartDate(),
                            p.getEndDate(),
                            !today.isBefore(p.getStartDate()) && !today.isAfter(p.getEndDate())
                    ))
                    .collect(Collectors.toList())
                : Collections.emptyList();

        return new ProductDetailResponse(
                detail.getId(),
                detail.isVat(),
                detail.isInStock(),
                detail.getShortDescription(),
                detail.getSummaryDescription(),
                detail.getDetailDescription(),
                detail.getFinalDescription(),
                detail.getTechnicalFunctions(),
                promotions,
                detail.getBrand() != null ? detail.getBrand().getId() : null,
                detail.getBrand() != null ? detail.getBrand().getName() : null,
                detail.getBrand() != null ? detail.getBrand().getLogo() : null,
                detail.getBrand() != null ? detail.getBrand().getLongDescription() : null
        );
    }

    public List<ProductCategory> listProductCategories() {
        return productCategoryRepository.findAll();
    }

    public List<Map<String, Object>> getProductTypesWithCategoriesAndBrands() {
        return productTypeRepository.findByEnabled(true).stream()
                .map(type -> {
                    Map<String, Object> typeMap = new HashMap<>();
                    typeMap.put("id", type.getId());
                    typeMap.put("syntax", type.getSyntax());

                    // Get categories for this type
                    List<Map<String, Object>> categories = productCategoryRepository
                            .findByProductTypeIdAndEnabled(type.getId(), true).stream()
                            .map(category -> {
                                Map<String, Object> catMap = new HashMap<>();
                                catMap.put("id", category.getId());
                                catMap.put("syntax", category.getSyntax());

                                // Get brands for products in this category
                                List<BrandResponse> brands = productRepository
                                        .findByProductCategoryIdAndEnabled(category.getId(), true).stream()
                                        .map(p -> p.getProductDetail())
                                        .filter(pd -> pd != null && pd.getBrand() != null)
                                        .map(pd -> pd.getBrand())
                                        .distinct()
                                        .map(this::toBrandResponse)
                                        .collect(Collectors.toList());

                                catMap.put("brands", brands);
                                return catMap;
                            })
                            .collect(Collectors.toList());

                    typeMap.put("categories", categories);
                    return typeMap;
                })
                .collect(Collectors.toList());
    }
}
