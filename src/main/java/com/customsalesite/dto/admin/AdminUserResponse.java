package com.customsalesite.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String tenantId;
    private boolean enabled;
    private LocalDateTime createdAt;
}
