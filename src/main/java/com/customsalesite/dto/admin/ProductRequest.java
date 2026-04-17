package com.customsalesite.dto.admin;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductRequest {
    private String syntax;
    private String description;
    private BigDecimal price;
    private boolean saleOff;
    private Integer salePercent;
    private List<String> images;
    private Long productTypeId;
    private boolean enabled = true;
}

