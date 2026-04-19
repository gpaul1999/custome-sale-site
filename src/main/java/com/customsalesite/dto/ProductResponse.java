package com.customsalesite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String syntax;
    private String description;
    private BigDecimal price;
    private boolean saleOff;
    private Integer salePercent;
    private BigDecimal salePrice;
    private List<String> images;
    private Long productTypeId;
    private String productTypeName;
    private Long productCategoryId;
    private String productCategoryName;
}

