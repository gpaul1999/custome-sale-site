package com.vtnguyen99.authservice.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.enabled:true}")
    private boolean firebaseEnabled;
    @Value("${firebase.service-account-path:classpath:firebase-service-account.json}")
    private String serviceAccountPath;

    @PostConstruct
    public void initialize() throws IOException {
        if (!firebaseEnabled) {
            log.warn("Firebase is disabled in configuration");
            return;
        }
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(
                                new ClassPathResource("firebase-service-account.json").getInputStream()
                        ))
                        .build();

                FirebaseApp.initializeApp(options);
                log.info("✅ Firebase initialized successfully!");
            } catch (IOException e) {
                log.error("❌ Failed to initialize Firebase: {}", e.getMessage());
                throw e;
            }
        }
    }
}
