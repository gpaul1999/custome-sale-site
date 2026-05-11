package com.customsalesite.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AdminPromotionResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean enabled;
    private Long productDetailId;
    private Long productId;
    private String productSyntax;
}
