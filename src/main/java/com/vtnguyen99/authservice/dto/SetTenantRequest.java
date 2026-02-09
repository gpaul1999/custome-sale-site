package com.vtnguyen99.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetTenantRequest {
    @NotBlank(message = "Tenant ID is required")
    private String tenantId;
}