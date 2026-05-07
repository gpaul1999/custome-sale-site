package com.customsalesite.dto.admin;

import lombok.Data;

@Data
public class ProductCategoryRequest {
    private String syntax;
    private String description;
    private Long productTypeId;
    private boolean enabled = true;
}
