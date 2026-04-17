package com.customsalesite.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandResponse {
    private Long id;
    private String name;
    private String logo;
    private List<String> longDescription;
}

