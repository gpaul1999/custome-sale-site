package com.vtnguyen99.authservice.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.vtnguyen99.authservice.dto.UserResponse;
import com.vtnguyen99.authservice.service.FirebaseAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final FirebaseAuthService authService;

    /**
     * Set tenant_id for a newly registered user
     * Call this after user registers via Firebase Client SDK
     */
    @PostMapping("/set-tenant")
    public ResponseEntity<?> setTenantClaim(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String idToken = extractToken(authHeader);
            FirebaseToken decodedToken = authService.verifyToken(idToken);
            String uid = decodedToken.getUid();
            authService.setTenantClaim(uid, tenantId);
            String customToken = authService.createCustomToken(uid, tenantId);
            return ResponseEntity.ok(Map.of(
                    "message", "Tenant claim set successfully",
                    "uid", uid,
                    "tenant_id", tenantId,
                    "custom_token", customToken
            ));
        } catch (FirebaseAuthException e) {
            log.error("Failed to set tenant claim: {}", e.getMessage());
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Authentication failed", "details", e.getMessage()));
        }
    }

    /**
     * Get current user info with tenant
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {

        try {
            String idToken = extractToken(authHeader);
            FirebaseToken decodedToken = authService.verifyToken(idToken);

            String uid = decodedToken.getUid();
            String email = decodedToken.getEmail();
            Boolean emailVerified = decodedToken.isEmailVerified();
            String tenantId = (String) decodedToken.getClaims().get("tenant_id");

            UserRecord userRecord = authService.getUserById(uid);

            UserResponse response = UserResponse.builder()
                    .uid(uid)
                    .email(email)
                    .emailVerified(emailVerified)
                    .tenantId(tenantId)
                    .displayName(userRecord.getDisplayName())
                    .photoUrl(userRecord.getPhotoUrl())
                    .createdAt(userRecord.getUserMetadata().getCreationTimestamp())
                    .lastSignInAt(userRecord.getUserMetadata().getLastSignInTimestamp())
                    .build();

            return ResponseEntity.ok(response);
        } catch (FirebaseAuthException e) {
            log.error("Failed to get user info: {}", e.getMessage());
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Authentication failed", "details", e.getMessage()));
        }
    }

    /**
     * Generate password reset link (for custom email sending)
     */
    @PostMapping("/admin/password-reset-link")
    public ResponseEntity<?> generatePasswordResetLink(
            @RequestParam String email,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        try {
            String link = authService.generatePasswordResetLink(email);
            return ResponseEntity.ok(Map.of(
                    "reset_link", link,
                    "email", email,
                    "tenant_id", tenantId
            ));
        } catch (FirebaseAuthException e) {
            log.error("Failed to generate password reset link: {}", e.getMessage());
            return ResponseEntity.status(400)
                    .body(Map.of("error", "Failed to generate link", "details", e.getMessage()));
        }
    }

    /**
     * Generate email verification link
     */
    @PostMapping("/admin/email-verification-link")
    public ResponseEntity<?> generateEmailVerificationLink(
            @RequestParam String email,
            @RequestHeader("X-Tenant-ID") String tenantId) {
        try {
            String link = authService.generateEmailVerificationLink(email);
            return ResponseEntity.ok(Map.of(
                    "verification_link", link,
                    "email", email,
                    "tenant_id", tenantId
            ));
        } catch (FirebaseAuthException e) {
            log.error("Failed to generate email verification link: {}", e.getMessage());
            return ResponseEntity.status(400)
                    .body(Map.of("error", "Failed to generate link", "details", e.getMessage()));
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/public/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "authentication-service",
                "firebase", "enabled"
        ));
    }

    // Helper method
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new IllegalArgumentException("Invalid Authorization header");
    }
}
