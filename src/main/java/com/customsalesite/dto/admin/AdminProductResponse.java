package com.customsalesite.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class AdminProductResponse {
    private Long id;
    private String syntax;
    private String description;
    private BigDecimal price;
    private boolean saleOff;
    private Integer salePercent;
    private List<String> images;
    private boolean enabled;
    private Long productCategoryId;
    private String productCategoryName;
    private Long productTypeId;
    private String productTypeName;
    // Detail fields
    private Long productDetailId;
    private boolean vat;
    private boolean inStock;
    private List<String> shortDescription;
    private String summaryDescription;
    private List<String> detailDescription;
    private String finalDescription;
    private List<String> technicalFunctions;
    private Long brandId;
    private String brandName;
}
