package com.checkout.payment.gateway.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.AquiringBankRequest;
import com.checkout.payment.gateway.model.AquiringBankResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class PaymentMapperTest {
    
    @Autowired
    PaymentMapper paymentMapper;

    PostPaymentRequest payment = new PostPaymentRequest();
    AquiringBankResponse abr = new AquiringBankResponse();

    @BeforeAll
    public void setUp() {
        payment.setAmount(10);
        payment.setCurrency("USD");
        payment.setExpiryMonth(12);
        payment.setExpiryYear(2024);
        payment.setCardNumber("123456789094321");
        payment.setCvv("123");
    }

    @Test
    void mapToAquiringBankRequestTest() {
        
        AquiringBankRequest abr1 = paymentMapper.mapToAquiringBankRequest(payment);

        assertEquals(abr1.getAmount(), 10);
        assertEquals(abr1.getCurrency(), "USD");
        assertEquals(abr1.getExpiryDate(), "12/2024");
        assertEquals(abr1.getCardNumber(), "123456789094321");
        assertEquals(abr1.getCvv(), "123");
    }

    @Test
    void mapToPostPaymentResponseAuthorisedTest() {
        abr.setAuthorized(true);

        PostPaymentResponse postPaymentResponse = paymentMapper.mapToPostPaymentResponse(abr, payment);

        assertEquals(postPaymentResponse.getAmount(), 10);
        assertEquals(postPaymentResponse.getCurrency(), "USD");
        assertEquals(postPaymentResponse.getExpiryMonth(), 12);
        assertEquals(postPaymentResponse.getExpiryYear(), 2024);
        assertEquals(postPaymentResponse.getCardNumberLastFour(), "4321");
        assertEquals(postPaymentResponse.getStatus(), PaymentStatus.AUTHORIZED);
    }

    @Test
    void mapToPostPaymentResponseDeclinedTest() {
        abr.setAuthorized(false);

        PostPaymentResponse postPaymentResponse = paymentMapper.mapToPostPaymentResponse(abr, payment);

        assertEquals(postPaymentResponse.getAmount(), 10);
        assertEquals(postPaymentResponse.getCurrency(), "USD");
        assertEquals(postPaymentResponse.getExpiryMonth(), 12);
        assertEquals(postPaymentResponse.getExpiryYear(), 2024);
        assertEquals(postPaymentResponse.getCardNumberLastFour(), "4321");
        assertEquals(postPaymentResponse.getStatus(), PaymentStatus.DECLINED);
    }


}
