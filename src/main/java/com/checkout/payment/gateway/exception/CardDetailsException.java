package com.checkout.payment.gateway.exception;

public class CardDetailsException extends RuntimeException {
    public CardDetailsException(String message) {
        super(message);
    }
}
