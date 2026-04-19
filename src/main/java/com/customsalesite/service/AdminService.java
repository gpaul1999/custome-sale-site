package com.customsalesite.service;

import com.customsalesite.dto.admin.*;
import com.customsalesite.entity.*;
import com.customsalesite.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductTypeRepository productTypeRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final ProductDetailRepository productDetailRepository;
    private final PromotionRepository promotionRepository;
    private final UserRepository userRepository;

    public List<ProductType> listProductTypes() {
        return productTypeRepository.findAll();
    }

    public List<ProductType> listEnabledProductTypes() {
        return productTypeRepository.findByEnabled(true);
    }

    public ProductType getProductType(Long id) {
        return productTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductType not found: " + id));
    }

    @Transactional
    public ProductType createProductType(ProductTypeRequest req) {
        ProductType pt = new ProductType();
        pt.setSyntax(req.getSyntax());
        pt.setDescription(req.getDescription());
        pt.setEnabled(req.isEnabled());
        return productTypeRepository.save(pt);
    }

    @Transactional
    public ProductType updateProductType(Long id, ProductTypeRequest req) {
        ProductType pt = getProductType(id);
        pt.setSyntax(req.getSyntax());
        pt.setDescription(req.getDescription());
        pt.setEnabled(req.isEnabled());
        return productTypeRepository.save(pt);
    }

    @Transactional
    public void toggleProductType(Long id) {
        ProductType pt = getProductType(id);
        pt.setEnabled(!pt.isEnabled());
        productTypeRepository.save(pt);
    }

    public List<Brand> listBrands() {
        return brandRepository.findAll();
    }

    public List<Brand> listEnabledBrands() {
        return brandRepository.findByEnabled(true);
    }

    public Brand getBrand(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found: " + id));
    }

    @Transactional
    public Brand createBrand(BrandRequest req) {
        Brand brand = new Brand();
        brand.setName(req.getName());
        brand.setLogo(req.getLogo());
        brand.setLongDescription(req.getLongDescription());
        brand.setEnabled(req.isEnabled());
        return brandRepository.save(brand);
    }

    @Transactional
    public Brand updateBrand(Long id, BrandRequest req) {
        Brand brand = getBrand(id);
        brand.setName(req.getName());
        brand.setLogo(req.getLogo());
        brand.setLongDescription(req.getLongDescription());
        brand.setEnabled(req.isEnabled());
        return brandRepository.save(brand);
    }

    @Transactional
    public void toggleBrand(Long id) {
        Brand brand = getBrand(id);
        brand.setEnabled(!brand.isEnabled());
        brandRepository.save(brand);
    }

    public List<Product> listProducts() {
        return productRepository.findAll();
    }

    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Transactional
    public void toggleProduct(Long id) {
        Product product = getProduct(id);
        product.setEnabled(!product.isEnabled());
        productRepository.save(product);
    }

    public List<ProductDetail> listProductDetailsOfEnabledProducts() {
        return productDetailRepository.findAll().stream()
                .filter(d -> d.getProduct() != null && d.getProduct().isEnabled())
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Promotion> listPromotions() {
        return promotionRepository.findAll();
    }

    public Promotion getPromotion(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found: " + id));
    }

    @Transactional
    public Promotion createPromotion(PromotionRequest req) {
        ProductDetail detail = productDetailRepository.findById(req.getProductDetailId())
                .orElseThrow(() -> new RuntimeException("ProductDetail not found: " + req.getProductDetailId()));
        Promotion promo = new Promotion();
        promo.setTitle(req.getTitle());
        promo.setDescription(req.getDescription());
        promo.setStartDate(req.getStartDate());
        promo.setEndDate(req.getEndDate());
        promo.setProductDetail(detail);
        promo.setEnabled(req.isEnabled());
        return promotionRepository.save(promo);
    }

    @Transactional
    public Promotion updatePromotion(Long id, PromotionRequest req) {
        Promotion promo = getPromotion(id);
        ProductDetail detail = productDetailRepository.findById(req.getProductDetailId())
                .orElseThrow(() -> new RuntimeException("ProductDetail not found: " + req.getProductDetailId()));
        promo.setTitle(req.getTitle());
        promo.setDescription(req.getDescription());
        promo.setStartDate(req.getStartDate());
        promo.setEndDate(req.getEndDate());
        promo.setProductDetail(detail);
        promo.setEnabled(req.isEnabled());
        return promotionRepository.save(promo);
    }

    @Transactional
    public void togglePromotion(Long id) {
        Promotion promo = promotionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotion not found: " + id));
        promo.setEnabled(!promo.isEnabled());
        promotionRepository.save(promo);
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
    }

    @Transactional
    public User updateUser(Long id, UserRequest req) {
        User user = getUser(id);
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setEnabled(req.isEnabled());
        return userRepository.save(user);
    }

    @Transactional
    public void toggleUser(Long id) {
        User user = getUser(id);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }


    public List<ProductCategory> listProductCategories() {
        return productCategoryRepository.findAll();
    }

    public com.customsalesite.entity.ProductCategory getProductCategory(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ProductCategory not found: " + id));
    }

    @Transactional
    public com.customsalesite.entity.ProductCategory saveProductCategory(com.customsalesite.entity.ProductCategory category) {
        return productCategoryRepository.save(category);
    }

    @Transactional
    public void toggleProductCategory(Long id) {
        var category = getProductCategory(id);
        category.setEnabled(!category.isEnabled());
        productCategoryRepository.save(category);
    }

    @Transactional
    public Product createProductFull(ProductFullRequest req) {
        ProductCategory category = productCategoryRepository.findById(req.getProductCategoryId())
                .orElseThrow(() -> new RuntimeException("ProductCategory not found: " + req.getProductCategoryId()));
        Product product = new Product();
        product.setSyntax(req.getSyntax());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setSaleOff(req.isSaleOff());
        product.setSalePercent(req.getSalePercent());
        product.setImages(req.getImages());
        product.setProductCategory(category);
        product.setEnabled(req.isEnabled());
        product = productRepository.save(product);

        // ProductDetail
        Brand brand = req.getBrandId() != null ? getBrand(req.getBrandId()) : null;
        ProductDetail detail = new ProductDetail();
        detail.setProduct(product);
        detail.setVat(req.isVat());
        detail.setInStock(req.isInStock());
        detail.setShortDescription(req.getShortDescription());
        detail.setSummaryDescription(req.getSummaryDescription());
        detail.setDetailDescription(req.getDetailDescription());
        detail.setFinalDescription(req.getFinalDescription());
        detail.setTechnicalFunctions(req.getTechnicalFunctions());
        detail.setBrand(brand);
        productDetailRepository.save(detail);
        return product;
    }

    @Transactional
    public Product updateProductFull(Long id, ProductFullRequest req) {
        Product product = getProduct(id);
        ProductCategory category = productCategoryRepository.findById(req.getProductCategoryId())
                .orElseThrow(() -> new RuntimeException("ProductCategory not found: " + req.getProductCategoryId()));
        product.setSyntax(req.getSyntax());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setSaleOff(req.isSaleOff());
        product.setSalePercent(req.getSalePercent());
        product.setImages(req.getImages());
        product.setProductCategory(category);
        product.setEnabled(req.isEnabled());
        product = productRepository.save(product);

        // ProductDetail
        ProductDetail detail = product.getProductDetail();
        if (detail == null) {
            detail = new ProductDetail();
            detail.setProduct(product);
        }
        Brand brand = req.getBrandId() != null ? getBrand(req.getBrandId()) : null;
        detail.setVat(req.isVat());
        detail.setInStock(req.isInStock());
        detail.setShortDescription(req.getShortDescription());
        detail.setSummaryDescription(req.getSummaryDescription());
        detail.setDetailDescription(req.getDetailDescription());
        detail.setFinalDescription(req.getFinalDescription());
        detail.setTechnicalFunctions(req.getTechnicalFunctions());
        detail.setBrand(brand);
        productDetailRepository.save(detail);
        return product;
    }
}
