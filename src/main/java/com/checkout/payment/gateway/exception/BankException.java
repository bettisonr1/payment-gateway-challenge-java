package com.checkout.payment.gateway.exception;

public class BankException extends RuntimeException {
    public BankException(String message) {
        super(message);
    }
}
