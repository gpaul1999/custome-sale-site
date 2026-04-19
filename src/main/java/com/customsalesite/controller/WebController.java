package com.customsalesite.controller;

import com.customsalesite.dto.BrandResponse;
import com.customsalesite.dto.ProductResponse;
import com.customsalesite.dto.ProductTypeResponse;
import com.customsalesite.service.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final DataService dataService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @GetMapping("/services")
    public String services(
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean saleOnly,
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false, defaultValue = "default") String sortBy,
            @RequestParam(required = false, defaultValue = "false") boolean filtered,
            Model model
    ) {
        List<ProductTypeResponse> productTypes = dataService.getAllProductTypes();

        Long activeTypeId = typeId;

        // If categoryId is provided, find its type
        if (categoryId != null && activeTypeId == null) {
            // Need to find which type owns this category
            for (ProductTypeResponse pt : productTypes) {
                var cats = dataService.getProductCategoriesByType(pt.getId());
                for (var cat : cats) {
                    if (cat.getId().equals(categoryId)) {
                        activeTypeId = pt.getId();
                        break;
                    }
                }
                if (activeTypeId != null) break;
            }
        }

        if (activeTypeId == null && !productTypes.isEmpty()) {
            activeTypeId = productTypes.get(0).getId();
        }

        // Compute the product list to display
        List<ProductResponse> activeProducts;

        if (filtered && keyword != null && !keyword.isBlank()) {
            activeProducts = dataService.searchProductsWithScore(keyword);
        } else if (categoryId != null) {
            activeProducts = dataService.getProductsByCategory(categoryId);
            filtered = true;
        } else if (filtered) {
            activeProducts = dataService.filterProducts(
                    activeTypeId, keyword, minPrice, maxPrice, saleOnly, brandId, sortBy);
        } else {
            activeProducts = activeTypeId != null ? dataService.getProductsByType(activeTypeId) : List.of();
        }

        // Get categories for active type
        List<com.customsalesite.dto.ProductCategoryResponse> categoriesForType =
                activeTypeId != null ? dataService.getProductCategoriesByType(activeTypeId) : List.of();

        // Brands available for the active tab (for dropdown)
        List<BrandResponse> brandsForType =
                activeTypeId != null ? dataService.getBrandsByProductType(activeTypeId) : List.of();

        // Resolve display names for breadcrumb
        final Long finalTypeId = activeTypeId;
        final Long finalCategoryId = categoryId;

        String activeTypeName = (finalTypeId != null) ? productTypes.stream()
                .filter(pt -> pt.getId().equals(finalTypeId))
                .map(ProductTypeResponse::getSyntax)
                .findFirst().orElse(null) : null;

        String activeCategoryName = (finalCategoryId != null) ? categoriesForType.stream()
                .filter(cat -> cat.getId().equals(finalCategoryId))
                .map(com.customsalesite.dto.ProductCategoryResponse::getSyntax)
                .findFirst().orElse(null) : null;

        model.addAttribute("productTypes", productTypes);
        model.addAttribute("activeProducts", activeProducts);
        model.addAttribute("activeTypeId", activeTypeId);
        model.addAttribute("activeTypeName", activeTypeName);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("activeCategoryName", activeCategoryName);
        model.addAttribute("categoriesForType", categoriesForType);
        model.addAttribute("brandsForType", brandsForType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("saleOnly", Boolean.TRUE.equals(saleOnly));
        model.addAttribute("brandId", brandId);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("filtered", filtered);
        return "services";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        DataService.ProductPageData data = dataService.getProductPageData(id);
        model.addAttribute("product", data.product());
        model.addAttribute("detail", data.detail());
        return "product-detail";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }
}
