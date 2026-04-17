package com.customsalesite.dto.admin;

import lombok.Data;

@Data
public class ProductTypeRequest {
    private String syntax;
    private String description;
    private boolean enabled = true;
}

