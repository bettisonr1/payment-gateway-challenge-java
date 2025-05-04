package com.checkout.payment.gateway.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AquiringBankRequest {
    
    @JsonProperty("card_number")
    private String cardNumber;
    @JsonProperty("expiry_date")
    private String expiryDate;
    private String currency;
    private Integer amount;
    private String cvv;

    public AquiringBankRequest() {

    }

    public AquiringBankRequest(
        String cardNumber, 
        String expiryDate,
        String currency,
        Integer amount,
        String cvv) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.currency = currency;
        this.amount = amount;
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    @Override
    public String toString() {
        return "AquiringBankRequest{" +
            "cardNumber=****" + cardNumber.substring(cardNumber.length() - 4) +
            ", expiryDate=" + expiryDate +
            ", currency='" + currency + '\'' +
            ", amount=" + amount +
            ", cvv=" + cvv +
            '}';
    }

}
