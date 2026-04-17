package com.customsalesite.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetailRequest {
    private Long productId;
    private boolean vat;
    private boolean inStock = true;
    private List<String> shortDescription;
    private String summaryDescription;
    private List<String> detailDescription;
    private String finalDescription;
    private List<String> technicalFunctions;
    private Long brandId;
}

