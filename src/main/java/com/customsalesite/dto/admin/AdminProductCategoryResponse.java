package com.customsalesite.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminProductCategoryResponse {
    private Long id;
    private String syntax;
    private String description;
    private boolean enabled;
    private Long productTypeId;
    private String productTypeName;
}
