package com.checkout.payment.gateway.utils;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.AquiringBankRequest;
import com.checkout.payment.gateway.model.AquiringBankResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;

@Service
public class PaymentMapper {
    
    public AquiringBankRequest mapToAquiringBankRequest(PostPaymentRequest paymentRequest) {
        AquiringBankRequest aquiringBankRequest = new AquiringBankRequest(
            paymentRequest.getCardNumber(),
            paymentRequest.getExpiryDate(),
            paymentRequest.getCurrency(),
            paymentRequest.getAmount(),
            paymentRequest.getCvv()
        );
        return aquiringBankRequest;
    }

    public PostPaymentResponse mapToPostPaymentResponse(AquiringBankResponse aquiringBankResponse, 
                                                        PostPaymentRequest postPaymentRequest) {
        PostPaymentResponse postPaymentResponse = new PostPaymentResponse(
            UUID.randomUUID(),
            aquiringBankResponse.getAuthorized()? PaymentStatus.AUTHORIZED : PaymentStatus.DECLINED,
            postPaymentRequest.getCardNumberLastFour(),
            postPaymentRequest.getExpiryMonth(), 
            postPaymentRequest.getExpiryYear(),
            postPaymentRequest.getCurrency(),
            postPaymentRequest.getAmount()
        );
        return postPaymentResponse;
    }

}
