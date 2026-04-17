package com.customsalesite.dto.admin;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductFullRequest {
    private String syntax;
    private String description;
    private BigDecimal price;
    private boolean saleOff;
    private Integer salePercent;
    private List<String> images;
    private Long productTypeId;
    private boolean enabled = true;
    private boolean vat;
    private boolean inStock = true;
    private List<String> shortDescription;
    private String summaryDescription;
    private List<String> detailDescription;
    private String finalDescription;
    private List<String> technicalFunctions;
    private Long brandId;
}

