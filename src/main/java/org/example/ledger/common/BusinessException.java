package org.example.ledger.common;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}