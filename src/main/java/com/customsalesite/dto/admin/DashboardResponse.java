package com.customsalesite.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponse {
    private long productTypeCount;
    private long categoryCount;
    private long brandCount;
    private long productCount;
    private long promotionCount;
    private long userCount;
}
