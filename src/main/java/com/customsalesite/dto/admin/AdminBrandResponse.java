package com.customsalesite.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AdminBrandResponse {
    private Long id;
    private String name;
    private String logo;
    private List<String> longDescription;
    private boolean enabled;
}
