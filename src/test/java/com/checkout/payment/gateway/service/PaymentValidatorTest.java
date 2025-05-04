package com.checkout.payment.gateway.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.checkout.payment.gateway.exception.CardDetailsException;
import com.checkout.payment.gateway.model.PostPaymentRequest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class PaymentValidatorTest {
    
    @Autowired
    private PaymentValidator paymentValidator;

    @Mock 
    private BindingResult bindingResult;

    @Mock
    private FieldError fieldError;

    private PostPaymentRequest postPaymentRequest;

    @BeforeAll
    public void setUp() {

        postPaymentRequest = new PostPaymentRequest();
        postPaymentRequest.setAmount(123);
        postPaymentRequest.setCardNumber("1234567898765");
        postPaymentRequest.setCurrency("EUR");
        postPaymentRequest.setCvv("123");
        
    }

    @Test
    void testBindingErrorsInvalidatePayment() {
        List<FieldError> fieldErrors = new ArrayList();
        fieldErrors.add(fieldError);
        when(fieldError.getField()).thenReturn("Test field");
        when(fieldError.getDefaultMessage()).thenReturn("validation error.");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        postPaymentRequest.setExpiryMonth(12);
        postPaymentRequest.setExpiryYear(2025);

        CardDetailsException ex = assertThrows(CardDetailsException.class, () -> paymentValidator.handleErrors(bindingResult, postPaymentRequest));
        assertEquals(ex.getMessage(), "Postpayment Request with card number last four: 8765 rejected with field validation errors");

    }

    @Test
    void testIsExpiryInFutureNegative() {
        when(bindingResult.hasErrors()).thenReturn(false);
        postPaymentRequest.setExpiryMonth(12);
        postPaymentRequest.setExpiryYear(2024);

        CardDetailsException ex = assertThrows(CardDetailsException.class, () -> paymentValidator.validate(postPaymentRequest));

        assertEquals(ex.getMessage(), "Expiry date not in future for card number last four: 8765");
    }

    @Test
    void testIsExpiryInFuturePositive() {
        when(bindingResult.hasErrors()).thenReturn(false);
        postPaymentRequest.setExpiryMonth(12);
        postPaymentRequest.setExpiryYear(2025);

        assertDoesNotThrow(() -> paymentValidator.validate(postPaymentRequest));

    }
}
