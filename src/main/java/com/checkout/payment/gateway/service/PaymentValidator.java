package com.checkout.payment.gateway.service;

import java.time.YearMonth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.checkout.payment.gateway.exception.CardDetailsException;
import com.checkout.payment.gateway.model.PostPaymentRequest;

@Service
public class PaymentValidator {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentValidator.class);
    
    public void handleErrors(BindingResult bindingResult, PostPaymentRequest postPaymentRequest) throws CardDetailsException {
        if(bindingResult.hasErrors()) {
            LOG.info("Postpayment Request with card number last four: " 
                + postPaymentRequest.getCardNumberLastFour() + 
                " rejected with following errors: ");
            bindingResult.getFieldErrors().forEach((err) -> {
                LOG.info(err.getField() + ": " + err.getDefaultMessage());
            });
        }

        throw new CardDetailsException("Postpayment Request with card number last four: " 
            + postPaymentRequest.getCardNumberLastFour() + 
            " rejected with field validation errors");
    }

    public void validate(PostPaymentRequest postPaymentRequest) {
        if(!isExpiryMonthInFuture(postPaymentRequest.getExpiryMonth(), 
            postPaymentRequest.getExpiryYear(), 
            postPaymentRequest.getCardNumberLastFour())) {
            throw new CardDetailsException("Expiry date not in future for card number last four: " 
                + postPaymentRequest.getCardNumberLastFour());
        }
    }

    private boolean isExpiryMonthInFuture(int expiryMonth, int expiryYear, String cardNumberLastFour) {
        YearMonth expiryDate = YearMonth.of(expiryYear, expiryMonth);
        boolean isExpiryMonthInFuture = expiryDate.isAfter(YearMonth.now());

        if(!isExpiryMonthInFuture) {
            LOG.info("Expiry date not in future for card number last four: " + cardNumberLastFour);
        }

        return isExpiryMonthInFuture;
    }
}
