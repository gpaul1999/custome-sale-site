package com.customsalesite.exception;

public class TenantIdMissingException extends RuntimeException {
    public TenantIdMissingException(String message) {
        super(message);
    }
}
