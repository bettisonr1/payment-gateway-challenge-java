package com.checkout.payment.gateway.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PostPaymentRequest implements Serializable {

  @JsonProperty("card_number_last_four")
  private String cardNumberLastFour;

  @NotNull
  @Size(min = 14, max = 19)
  @Pattern(regexp="\\d+")
  @JsonProperty("card_number")
  private String cardNumber;

  @NotNull
  @JsonProperty("expiry_month")
  @Range(min=1, max=12)
  private int expiryMonth;

  @NotNull
  @JsonProperty("expiry_year")
  private int expiryYear;

  @Length(min = 3, max = 3, message = "currency must be 3 characters")
  @Pattern(regexp = "GBP|USD|EUR", message = "currency must be GBP, USD, or EUR")
  private String currency;

  @NotNull
  @Min(1)
  private Integer amount;

  @NotNull
  @Pattern(regexp="\\d+")
  @Size(min = 3, max = 4)
  private String cvv;

  public PostPaymentRequest(String cardNumber, 
                            int expiryMonth, 
                            int expiryYear, 
                            String currency,
                            int amount,
                            String cvv) {
    this.cardNumber = cardNumber;
    this.expiryMonth = expiryMonth;
    this.expiryYear = expiryYear;
    this.currency = currency;
    this.amount = amount;
    this.cvv = cvv;
  }

  public PostPaymentRequest() {
    
  }

  public String getCardNumberLastFour() {
    return cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
  }

  public void setCardNumberLastFour(String cardNumberLastFour) {
    this.cardNumberLastFour = cardNumberLastFour;
  }

  public int getExpiryMonth() {
    return expiryMonth;
  }

  public void setExpiryMonth(int expiryMonth) {
    this.expiryMonth = expiryMonth;
  }

  public int getExpiryYear() {
    return expiryYear;
  }

  public void setExpiryYear(int expiryYear) {
    this.expiryYear = expiryYear;
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

  @JsonProperty("expiry_date")
  public String getExpiryDate() {
    return String.format("%d/%d", expiryMonth, expiryYear);
  }

  @Override
  public String toString() {
    return "PostPaymentRequest{" +
        "cardNumberLastFour=" + cardNumberLastFour +
        ", expiryMonth=" + expiryMonth +
        ", expiryYear=" + expiryYear +
        ", currency='" + currency + '\'' +
        ", amount=" + amount +
        ", cvv=" + cvv +
        '}';
  }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
