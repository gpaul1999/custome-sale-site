package com.customsalesite.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class BrandRequest {
    private String name;
    private String logo;
    private List<String> longDescription;
    private boolean enabled = true;
}

