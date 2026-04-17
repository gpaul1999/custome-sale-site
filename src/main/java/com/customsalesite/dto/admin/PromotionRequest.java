package com.customsalesite.dto.admin;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PromotionRequest {
    private Long productDetailId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean enabled = true;
}

