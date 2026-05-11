package com.customsalesite.controller;

import com.customsalesite.dto.admin.*;
import com.customsalesite.entity.*;
import com.customsalesite.security.JwtUtil;
import com.customsalesite.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService adminUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    // ── Auth ──────────────────────────────────────────────────────────────────

    @PostMapping("/auth/login")
    public ResponseEntity<AdminLoginResponse> login(@RequestBody AdminLoginRequest req) {
        var userDetails = adminUserDetailsService.loadUserByUsername(req.getUsername());
        if (userDetails == null || !passwordEncoder.matches(req.getPassword(), userDetails.getPassword())) {
            return ResponseEntity.status(401).build();
        }
        String token = jwtUtil.generateAdminToken(userDetails.getUsername());
        return ResponseEntity.ok(new AdminLoginResponse(token, userDetails.getUsername()));
    }

    // ── Dashboard ─────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> dashboard() {
        return ResponseEntity.ok(new DashboardResponse(
                adminService.listProductTypes().size(),
                adminService.listProductCategories().size(),
                adminService.listBrands().size(),
                adminService.listProducts().size(),
                adminService.listPromotions().size(),
                adminService.listUsers().size()
        ));
    }

    // ── Product Types ─────────────────────────────────────────────────────────

    @GetMapping("/product-types")
    public ResponseEntity<List<AdminProductTypeResponse>> listProductTypes() {
        return ResponseEntity.ok(
                adminService.listProductTypes().stream()
                        .map(this::toProductTypeResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/product-types")
    public ResponseEntity<AdminProductTypeResponse> createProductType(@RequestBody ProductTypeRequest req) {
        return ResponseEntity.ok(toProductTypeResponse(adminService.createProductType(req)));
    }

    @GetMapping("/product-types/{id}")
    public ResponseEntity<AdminProductTypeResponse> getProductType(@PathVariable Long id) {
        return ResponseEntity.ok(toProductTypeResponse(adminService.getProductType(id)));
    }

    @PutMapping("/product-types/{id}")
    public ResponseEntity<AdminProductTypeResponse> updateProductType(@PathVariable Long id,
                                                                      @RequestBody ProductTypeRequest req) {
        return ResponseEntity.ok(toProductTypeResponse(adminService.updateProductType(id, req)));
    }

    @PostMapping("/product-types/{id}/toggle")
    public ResponseEntity<Void> toggleProductType(@PathVariable Long id) {
        adminService.toggleProductType(id);
        return ResponseEntity.ok().build();
    }

    // ── Product Categories ────────────────────────────────────────────────────

    @GetMapping("/product-categories")
    public ResponseEntity<List<AdminProductCategoryResponse>> listProductCategories() {
        return ResponseEntity.ok(
                adminService.listProductCategories().stream()
                        .map(this::toCategoryResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/product-categories")
    public ResponseEntity<AdminProductCategoryResponse> createProductCategory(@RequestBody ProductCategoryRequest req) {
        ProductCategory cat = new ProductCategory();
        cat.setSyntax(req.getSyntax());
        cat.setDescription(req.getDescription());
        cat.setEnabled(req.isEnabled());
        if (req.getProductTypeId() != null) {
            cat.setProductType(adminService.getProductType(req.getProductTypeId()));
        }
        return ResponseEntity.ok(toCategoryResponse(adminService.saveProductCategory(cat)));
    }

    @GetMapping("/product-categories/{id}")
    public ResponseEntity<AdminProductCategoryResponse> getProductCategory(@PathVariable Long id) {
        return ResponseEntity.ok(toCategoryResponse(adminService.getProductCategory(id)));
    }

    @PutMapping("/product-categories/{id}")
    public ResponseEntity<AdminProductCategoryResponse> updateProductCategory(@PathVariable Long id,
                                                                               @RequestBody ProductCategoryRequest req) {
        ProductCategory cat = adminService.getProductCategory(id);
        cat.setSyntax(req.getSyntax());
        cat.setDescription(req.getDescription());
        cat.setEnabled(req.isEnabled());
        if (req.getProductTypeId() != null) {
            cat.setProductType(adminService.getProductType(req.getProductTypeId()));
        }
        return ResponseEntity.ok(toCategoryResponse(adminService.saveProductCategory(cat)));
    }

    @PostMapping("/product-categories/{id}/toggle")
    public ResponseEntity<Void> toggleProductCategory(@PathVariable Long id) {
        adminService.toggleProductCategory(id);
        return ResponseEntity.ok().build();
    }

    // ── Brands ────────────────────────────────────────────────────────────────

    @GetMapping("/brands")
    public ResponseEntity<List<AdminBrandResponse>> listBrands() {
        return ResponseEntity.ok(
                adminService.listBrands().stream()
                        .map(this::toBrandResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/brands")
    public ResponseEntity<AdminBrandResponse> createBrand(@RequestBody BrandRequest req) {
        return ResponseEntity.ok(toBrandResponse(adminService.createBrand(req)));
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<AdminBrandResponse> getBrand(@PathVariable Long id) {
        return ResponseEntity.ok(toBrandResponse(adminService.getBrand(id)));
    }

    @PutMapping("/brands/{id}")
    public ResponseEntity<AdminBrandResponse> updateBrand(@PathVariable Long id, @RequestBody BrandRequest req) {
        return ResponseEntity.ok(toBrandResponse(adminService.updateBrand(id, req)));
    }

    @PostMapping("/brands/{id}/toggle")
    public ResponseEntity<Void> toggleBrand(@PathVariable Long id) {
        adminService.toggleBrand(id);
        return ResponseEntity.ok().build();
    }

    // ── Products ──────────────────────────────────────────────────────────────

    @GetMapping("/products")
    public ResponseEntity<List<AdminProductResponse>> listProducts() {
        return ResponseEntity.ok(
                adminService.listProducts().stream()
                        .map(this::toAdminProductResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/products")
    public ResponseEntity<AdminProductResponse> createProduct(@RequestBody ProductFullRequest req) {
        return ResponseEntity.ok(toAdminProductResponse(adminService.createProductFull(req)));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<AdminProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(toAdminProductResponse(adminService.getProduct(id)));
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<AdminProductResponse> updateProduct(@PathVariable Long id,
                                                               @RequestBody ProductFullRequest req) {
        return ResponseEntity.ok(toAdminProductResponse(adminService.updateProductFull(id, req)));
    }

    @PostMapping("/products/{id}/toggle")
    public ResponseEntity<Void> toggleProduct(@PathVariable Long id) {
        adminService.toggleProduct(id);
        return ResponseEntity.ok().build();
    }

    // ── Promotions ────────────────────────────────────────────────────────────

    @GetMapping("/promotions")
    public ResponseEntity<List<AdminPromotionResponse>> listPromotions() {
        return ResponseEntity.ok(
                adminService.listPromotions().stream()
                        .map(this::toPromotionResponse)
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/promotions")
    public ResponseEntity<AdminPromotionResponse> createPromotion(@RequestBody PromotionRequest req) {
        return ResponseEntity.ok(toPromotionResponse(adminService.createPromotion(req)));
    }

    @GetMapping("/promotions/{id}")
    public ResponseEntity<AdminPromotionResponse> getPromotion(@PathVariable Long id) {
        return ResponseEntity.ok(toPromotionResponse(adminService.getPromotion(id)));
    }

    @PutMapping("/promotions/{id}")
    public ResponseEntity<AdminPromotionResponse> updatePromotion(@PathVariable Long id,
                                                                   @RequestBody PromotionRequest req) {
        return ResponseEntity.ok(toPromotionResponse(adminService.updatePromotion(id, req)));
    }

    @PostMapping("/promotions/{id}/toggle")
    public ResponseEntity<Void> togglePromotion(@PathVariable Long id) {
        adminService.togglePromotion(id);
        return ResponseEntity.ok().build();
    }

    // ── Users ─────────────────────────────────────────────────────────────────

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> listUsers() {
        return ResponseEntity.ok(
                adminService.listUsers().stream()
                        .map(this::toUserResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<AdminUserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(toUserResponse(adminService.getUser(id)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<AdminUserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest req) {
        return ResponseEntity.ok(toUserResponse(adminService.updateUser(id, req)));
    }

    @PostMapping("/users/{id}/toggle")
    public ResponseEntity<Void> toggleUser(@PathVariable Long id) {
        adminService.toggleUser(id);
        return ResponseEntity.ok().build();
    }

    // ── Helper: product-detail list for promotions form ───────────────────────

    @GetMapping("/product-details")
    public ResponseEntity<List<java.util.Map<String, Object>>> listProductDetails() {
        return ResponseEntity.ok(
                adminService.listProductDetailsOfEnabledProducts().stream()
                        .map(d -> {
                            var m = new java.util.HashMap<String, Object>();
                            m.put("id", d.getId());
                            m.put("productId", d.getProduct() != null ? d.getProduct().getId() : null);
                            m.put("productSyntax", d.getProduct() != null ? d.getProduct().getSyntax() : null);
                            return m;
                        })
                        .collect(Collectors.toList())
        );
    }

    // ── Helper: enabled product types / brands for category/product forms ─────

    @GetMapping("/enabled-product-types")
    public ResponseEntity<List<AdminProductTypeResponse>> listEnabledProductTypes() {
        return ResponseEntity.ok(
                adminService.listEnabledProductTypes().stream()
                        .map(this::toProductTypeResponse)
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/enabled-brands")
    public ResponseEntity<List<AdminBrandResponse>> listEnabledBrands() {
        return ResponseEntity.ok(
                adminService.listEnabledBrands().stream()
                        .map(this::toBrandResponse)
                        .collect(Collectors.toList())
        );
    }

    // ── Mapping helpers ───────────────────────────────────────────────────────

    private AdminProductTypeResponse toProductTypeResponse(ProductType pt) {
        return new AdminProductTypeResponse(pt.getId(), pt.getSyntax(), pt.getDescription(), pt.isEnabled());
    }

    private AdminBrandResponse toBrandResponse(Brand b) {
        return new AdminBrandResponse(b.getId(), b.getName(), b.getLogo(), b.getLongDescription(), b.isEnabled());
    }

    private AdminProductCategoryResponse toCategoryResponse(ProductCategory c) {
        Long typeId = c.getProductType() != null ? c.getProductType().getId() : null;
        String typeName = c.getProductType() != null ? c.getProductType().getSyntax() : null;
        return new AdminProductCategoryResponse(c.getId(), c.getSyntax(), c.getDescription(),
                c.isEnabled(), typeId, typeName);
    }

    private AdminProductResponse toAdminProductResponse(Product p) {
        var detail = p.getProductDetail();
        Long detailId = detail != null ? detail.getId() : null;
        boolean vat = detail != null && detail.isVat();
        boolean inStock = detail == null || detail.isInStock();
        var shortDesc = detail != null ? detail.getShortDescription() : null;
        String summaryDesc = detail != null ? detail.getSummaryDescription() : null;
        var detailDesc = detail != null ? detail.getDetailDescription() : null;
        String finalDesc = detail != null ? detail.getFinalDescription() : null;
        var techFuncs = detail != null ? detail.getTechnicalFunctions() : null;
        Long brandId = (detail != null && detail.getBrand() != null) ? detail.getBrand().getId() : null;
        String brandName = (detail != null && detail.getBrand() != null) ? detail.getBrand().getName() : null;

        Long catId = p.getProductCategory() != null ? p.getProductCategory().getId() : null;
        String catName = p.getProductCategory() != null ? p.getProductCategory().getSyntax() : null;
        Long typeId = (p.getProductCategory() != null && p.getProductCategory().getProductType() != null)
                ? p.getProductCategory().getProductType().getId() : null;
        String typeName = (p.getProductCategory() != null && p.getProductCategory().getProductType() != null)
                ? p.getProductCategory().getProductType().getSyntax() : null;

        return new AdminProductResponse(p.getId(), p.getSyntax(), p.getDescription(), p.getPrice(),
                p.isSaleOff(), p.getSalePercent(), p.getImages(), p.isEnabled(),
                catId, catName, typeId, typeName,
                detailId, vat, inStock, shortDesc, summaryDesc, detailDesc, finalDesc, techFuncs, brandId, brandName);
    }

    private AdminPromotionResponse toPromotionResponse(Promotion promo) {
        var detail = promo.getProductDetail();
        Long detailId = detail != null ? detail.getId() : null;
        Long productId = (detail != null && detail.getProduct() != null) ? detail.getProduct().getId() : null;
        String productSyntax = (detail != null && detail.getProduct() != null)
                ? detail.getProduct().getSyntax() : null;
        return new AdminPromotionResponse(promo.getId(), promo.getTitle(), promo.getDescription(),
                promo.getStartDate(), promo.getEndDate(), promo.isEnabled(), detailId, productId, productSyntax);
    }

    private AdminUserResponse toUserResponse(com.customsalesite.entity.User u) {
        return new AdminUserResponse(u.getId(), u.getFirstName(), u.getLastName(),
                u.getEmail(), u.getTenantId(), u.isEnabled(), u.getCreatedAt());
    }
}
