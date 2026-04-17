package com.customsalesite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailResponse {
    private Long id;
    private boolean isVat;
    private boolean inStock;
    private List<String> shortDescription;
    private String summaryDescription;
    private List<String> detailDescription;
    private String finalDescription;
    private List<String> technicalFunctions;
    private List<PromotionResponse> promotions;
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private List<String> brandLongDescription;
}

