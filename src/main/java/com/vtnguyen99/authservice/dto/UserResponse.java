package com.vtnguyen99.authservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private String uid;
    private String email;
    private Boolean emailVerified;
    private String tenantId;
    private String displayName;
    private String photoUrl;
    private Long createdAt;
    private Long lastSignInAt;
}
