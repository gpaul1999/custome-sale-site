package com.customsalesite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryResponse {
    private Long id;
    private String syntax;
    private String description;
    private Long productTypeId;
}

