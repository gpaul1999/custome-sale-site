package com.customsalesite.dto.admin;

import lombok.Data;

@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String tenantId;
    private String email;
    private boolean enabled = true;
}

