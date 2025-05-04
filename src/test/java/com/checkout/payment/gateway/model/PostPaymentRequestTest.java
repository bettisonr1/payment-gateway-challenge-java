package com.checkout.payment.gateway.model;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class PostPaymentRequestTest {

    private Validator validator;

    private PostPaymentRequest postPaymentRequest;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        postPaymentRequest = new PostPaymentRequest(
            "12345678989987",
            8,
            2025,
            "GBP",
            56,
            "123"
        );
    }

    @Test
    void testValidRequest() {
    
        Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
            postPaymentRequest);
        assertTrue(violations.isEmpty());
        
    }

    @Test
    void testLongCardNumber() {

        postPaymentRequest.setCardNumber("2345678909876598788888");
        Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
            postPaymentRequest);

        ConstraintViolation constraintViolation = violations.iterator().next();
        assertEquals(constraintViolation.getPropertyPath().toString(), "cardNumber");
        assertEquals(constraintViolation.getMessage(), "size must be between 14 and 19");

    }

    @Test
    void testShortCardNumber() {
        postPaymentRequest.setCardNumber("59878");
        Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
            postPaymentRequest);

        ConstraintViolation constraintViolation = violations.iterator().next();
        assertEquals(constraintViolation.getPropertyPath().toString(), "cardNumber");
        assertEquals(constraintViolation.getMessage(), "size must be between 14 and 19");
    }

    @Test
    void nonNumericCardNumber() {
        postPaymentRequest.setCardNumber("56jddsfmsf34324");
        Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
            postPaymentRequest);

        ConstraintViolation constraintViolation = violations.iterator().next();
        assertEquals(constraintViolation.getPropertyPath().toString(), "cardNumber");
        assertEquals(constraintViolation.getMessage(), "must match \"\\d+\"");
    }

    @Test
    void testYearMonthFormatting() {
        assertEquals(postPaymentRequest.getExpiryDate(), "8/2025");
    }

    @Test
    void testCvvTooLong() {
        postPaymentRequest.setCvv("123456");

        Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
            postPaymentRequest);

        ConstraintViolation constraintViolation = violations.iterator().next();
        assertEquals(constraintViolation.getPropertyPath().toString(), "cvv");
        assertEquals(constraintViolation.getMessage(), "size must be between 3 and 4");
    }

    @Test
    void testCurrencyNotSupported() {
        postPaymentRequest.setCurrency("AUD");

        Set<ConstraintViolation<PostPaymentRequest>> violations = validator.validate(
            postPaymentRequest);

        ConstraintViolation constraintViolation = violations.iterator().next();
        assertEquals(constraintViolation.getPropertyPath().toString(), "currency");
        assertEquals(constraintViolation.getMessage(), "currency must be GBP, USD, or EUR");
    }


    // TODO add more test cases here
}
