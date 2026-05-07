package com.customsalesite.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminProductTypeResponse {
    private Long id;
    private String syntax;
    private String description;
    private boolean enabled;
}
