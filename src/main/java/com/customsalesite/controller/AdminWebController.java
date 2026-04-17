package com.customsalesite.controller;

import com.customsalesite.dto.admin.*;
import com.customsalesite.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminWebController {

    private final AdminService adminService;

    @GetMapping({"", "/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("productTypeCount", adminService.listProductTypes().size());
        model.addAttribute("brandCount",       adminService.listBrands().size());
        model.addAttribute("productCount",     adminService.listProducts().size());
        model.addAttribute("userCount",        adminService.listUsers().size());
        return "admin/dashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/product-types")
    public String productTypeList(Model model) {
        model.addAttribute("items", adminService.listProductTypes());
        return "admin/product-type/list";
    }

    @GetMapping("/product-types/new")
    public String productTypeNew(Model model) {
        model.addAttribute("form", new ProductTypeRequest());
        model.addAttribute("editMode", false);
        return "admin/product-type/form";
    }

    @PostMapping("/product-types/new")
    public String productTypeCreate(@ModelAttribute("form") ProductTypeRequest req,
                                    RedirectAttributes ra) {
        adminService.createProductType(req);
        ra.addFlashAttribute("successMsg", "Đã tạo loại sản phẩm thành công.");
        return "redirect:/admin/product-types";
    }

    @GetMapping("/product-types/{id}/edit")
    public String productTypeEdit(@PathVariable Long id, Model model) {
        var pt = adminService.getProductType(id);
        var form = new ProductTypeRequest();
        form.setSyntax(pt.getSyntax());
        form.setDescription(pt.getDescription());
        form.setEnabled(pt.isEnabled());
        model.addAttribute("form", form);
        model.addAttribute("editMode", true);
        model.addAttribute("id", id);
        return "admin/product-type/form";
    }

    @PostMapping("/product-types/{id}/edit")
    public String productTypeUpdate(@PathVariable Long id,
                                    @ModelAttribute("form") ProductTypeRequest req,
                                    RedirectAttributes ra) {
        adminService.updateProductType(id, req);
        ra.addFlashAttribute("successMsg", "Đã cập nhật loại sản phẩm.");
        return "redirect:/admin/product-types";
    }

    @PostMapping("/product-types/{id}/toggle")
    public String productTypeToggle(@PathVariable Long id, RedirectAttributes ra) {
        adminService.toggleProductType(id);
        ra.addFlashAttribute("successMsg", "Đã thay đổi trạng thái.");
        return "redirect:/admin/product-types";
    }

    @GetMapping("/brands")
    public String brandList(Model model) {
        model.addAttribute("items", adminService.listBrands());
        return "admin/brand/list";
    }

    @GetMapping("/brands/new")
    public String brandNew(Model model) {
        model.addAttribute("form", new BrandRequest());
        model.addAttribute("editMode", false);
        return "admin/brand/form";
    }

    @PostMapping("/brands/new")
    public String brandCreate(@ModelAttribute("form") BrandRequest req, RedirectAttributes ra) {
        adminService.createBrand(req);
        ra.addFlashAttribute("successMsg", "Đã tạo thương hiệu thành công.");
        return "redirect:/admin/brands";
    }

    @GetMapping("/brands/{id}/edit")
    public String brandEdit(@PathVariable Long id, Model model) {
        var brand = adminService.getBrand(id);
        var form = new BrandRequest();
        form.setName(brand.getName());
        form.setLogo(brand.getLogo());
        form.setLongDescription(brand.getLongDescription());
        form.setEnabled(brand.isEnabled());
        model.addAttribute("form", form);
        model.addAttribute("editMode", true);
        model.addAttribute("id", id);
        return "admin/brand/form";
    }

    @PostMapping("/brands/{id}/edit")
    public String brandUpdate(@PathVariable Long id,
                              @ModelAttribute("form") BrandRequest req,
                              RedirectAttributes ra) {
        adminService.updateBrand(id, req);
        ra.addFlashAttribute("successMsg", "Đã cập nhật thương hiệu.");
        return "redirect:/admin/brands";
    }

    @PostMapping("/brands/{id}/toggle")
    public String brandToggle(@PathVariable Long id, RedirectAttributes ra) {
        adminService.toggleBrand(id);
        ra.addFlashAttribute("successMsg", "Đã thay đổi trạng thái.");
        return "redirect:/admin/brands";
    }

    @GetMapping("/products")
    public String productList(Model model) {
        model.addAttribute("items", adminService.listProducts());
        return "admin/product/list";
    }

    @GetMapping("/products/new")
    public String productNew(Model model) {
        model.addAttribute("form", new ProductFullRequest());
        model.addAttribute("productTypes", adminService.listEnabledProductTypes());
        model.addAttribute("brands", adminService.listEnabledBrands());
        model.addAttribute("editMode", false);
        return "admin/product/form";
    }

    @PostMapping("/products/new")
    public String productCreate(@ModelAttribute("form") ProductFullRequest req, RedirectAttributes ra) {
        adminService.createProductFull(req);
        ra.addFlashAttribute("successMsg", "Đã tạo sản phẩm thành công.");
        return "redirect:/admin/products";
    }

    @GetMapping("/products/{id}/edit")
    public String productEdit(@PathVariable Long id, Model model) {
        var product = adminService.getProduct(id);
        var detail = product.getProductDetail();
        var form = new ProductFullRequest();
        form.setSyntax(product.getSyntax());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setSaleOff(product.isSaleOff());
        form.setSalePercent(product.getSalePercent());
        form.setImages(product.getImages());
        form.setProductTypeId(product.getProductType().getId());
        form.setEnabled(product.isEnabled());
        if (detail != null) {
            form.setVat(detail.isVat());
            form.setInStock(detail.isInStock());
            form.setShortDescription(detail.getShortDescription());
            form.setSummaryDescription(detail.getSummaryDescription());
            form.setDetailDescription(detail.getDetailDescription());
            form.setFinalDescription(detail.getFinalDescription());
            form.setTechnicalFunctions(detail.getTechnicalFunctions());
            form.setBrandId(detail.getBrand() != null ? detail.getBrand().getId() : null);
        }
        model.addAttribute("form", form);
        model.addAttribute("productTypes", adminService.listEnabledProductTypes());
        model.addAttribute("brands", adminService.listEnabledBrands());
        model.addAttribute("editMode", true);
        model.addAttribute("id", id);
        return "admin/product/form";
    }

    @PostMapping("/products/{id}/edit")
    public String productUpdate(@PathVariable Long id,
                                @ModelAttribute("form") ProductFullRequest req,
                                RedirectAttributes ra) {
        adminService.updateProductFull(id, req);
        ra.addFlashAttribute("successMsg", "Đã cập nhật sản phẩm.");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/{id}/toggle")
    public String productToggle(@PathVariable Long id, RedirectAttributes ra) {
        adminService.toggleProduct(id);
        ra.addFlashAttribute("successMsg", "Đã thay đổi trạng thái.");
        return "redirect:/admin/products";
    }

    @GetMapping("/promotions")
    public String promotionList(Model model) {
        model.addAttribute("items", adminService.listPromotions());
        return "admin/promotion/list";
    }

    @GetMapping("/promotions/new")
    public String promotionNew(Model model) {
        model.addAttribute("form", new PromotionRequest());
        model.addAttribute("productDetails", adminService.listProductDetailsOfEnabledProducts());
        model.addAttribute("editMode", false);
        return "admin/promotion/form";
    }

    @PostMapping("/promotions/new")
    public String promotionCreate(@ModelAttribute("form") PromotionRequest req,
                                  RedirectAttributes ra) {
        adminService.createPromotion(req);
        ra.addFlashAttribute("successMsg", "Đã tạo khuyến mãi thành công.");
        return "redirect:/admin/promotions";
    }

    @GetMapping("/promotions/{id}/edit")
    public String promotionEdit(@PathVariable Long id, Model model) {
        var promo = adminService.getPromotion(id);
        var form = new PromotionRequest();
        form.setProductDetailId(promo.getProductDetail().getId());
        form.setTitle(promo.getTitle());
        form.setDescription(promo.getDescription());
        form.setStartDate(promo.getStartDate());
        form.setEndDate(promo.getEndDate());
        form.setEnabled(promo.isEnabled());
        model.addAttribute("form", form);
        model.addAttribute("productDetails", adminService.listProductDetailsOfEnabledProducts());
        model.addAttribute("editMode", true);
        model.addAttribute("id", id);
        return "admin/promotion/form";
    }

    @PostMapping("/promotions/{id}/edit")
    public String promotionUpdate(@PathVariable Long id,
                                  @ModelAttribute("form") PromotionRequest req,
                                  RedirectAttributes ra) {
        adminService.updatePromotion(id, req);
        ra.addFlashAttribute("successMsg", "Đã cập nhật khuyến mãi.");
        return "redirect:/admin/promotions";
    }

    @PostMapping("/promotions/{id}/toggle")
    public String promotionToggle(@PathVariable Long id, RedirectAttributes ra) {
        adminService.togglePromotion(id);
        ra.addFlashAttribute("successMsg", "Đã thay đổi trạng thái.");
        return "redirect:/admin/promotions";
    }

    @GetMapping("/users")
    public String userList(Model model) {
        model.addAttribute("items", adminService.listUsers());
        return "admin/user/list";
    }

    @GetMapping("/users/{id}/edit")
    public String userEdit(@PathVariable Long id, Model model) {
        var user = adminService.getUser(id);
        var form = new UserRequest();
        form.setFirstName(user.getFirstName());
        form.setLastName(user.getLastName());
        form.setTenantId(user.getTenantId());
        form.setEmail(user.getEmail());
        form.setEnabled(user.isEnabled());
        model.addAttribute("form", form);
        model.addAttribute("editMode", true);
        model.addAttribute("id", id);
        return "admin/user/form";
    }

    @PostMapping("/users/{id}/edit")
    public String userUpdate(@PathVariable Long id,
                             @ModelAttribute("form") UserRequest req,
                             RedirectAttributes ra) {
        adminService.updateUser(id, req);
        ra.addFlashAttribute("successMsg", "Đã cập nhật người dùng.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String userToggle(@PathVariable Long id, RedirectAttributes ra) {
        adminService.toggleUser(id);
        ra.addFlashAttribute("successMsg", "Đã thay đổi trạng thái.");
        return "redirect:/admin/users";
    }
}

