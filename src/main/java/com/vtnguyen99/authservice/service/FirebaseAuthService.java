package com.vtnguyen99.authservice.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FirebaseAuthService {

    private final FirebaseAuth firebaseAuth;

    public FirebaseAuthService() {
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    /**
     * Set tenant_id as custom claim for a user
     */
    public void setTenantClaim(String uid, String tenantId) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tenant_id", tenantId);
        firebaseAuth.setCustomUserClaims(uid, claims);
        log.info("Set tenant_id '{}' for user {}", tenantId, uid);
    }

    /**
     * Verify Firebase ID token
     */
    public FirebaseToken verifyToken(String idToken) throws FirebaseAuthException {
        try {
            return firebaseAuth.verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            log.error("Token verification failed: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Get user by UID
     */
    public UserRecord getUserById(String uid) throws FirebaseAuthException {
        return firebaseAuth.getUser(uid);
    }

    /**
     * Get user by email and tenant
     */
    public UserRecord getUserByEmail(String email) throws FirebaseAuthException {
        return firebaseAuth.getUserByEmail(email);
    }

    /**
     * Create a custom token with tenant_id
     */
    public String createCustomToken(String uid, String tenantId) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tenant_id", tenantId);
        return firebaseAuth.createCustomToken(uid, claims);
    }

    /**
     * Generate password reset link
     */
    public String generatePasswordResetLink(String email) throws FirebaseAuthException {
        return firebaseAuth.generatePasswordResetLink(email);
    }

    /**
     * Generate email verification link
     */
    public String generateEmailVerificationLink(String email) throws FirebaseAuthException {
        return firebaseAuth.generateEmailVerificationLink(email);
    }

    /**
     * Delete user
     */
    public void deleteUser(String uid) throws FirebaseAuthException {
        firebaseAuth.deleteUser(uid);
        log.info("Deleted user {}", uid);
    }

    /**
     * Update user email
     */
    public void updateEmail(String uid, String newEmail) throws FirebaseAuthException {
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(uid)
                .setEmail(newEmail)
                .setEmailVerified(false);
        firebaseAuth.updateUser(request);
        log.info("Updated email for user {}", uid);
    }
}
