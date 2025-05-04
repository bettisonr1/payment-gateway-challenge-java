package com.checkout.payment.gateway.controller;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentGatewayControllerTest {

  @Autowired
  private MockMvc mvc;
  @Autowired
  PaymentsRepository paymentsRepository;
  @Autowired
  private ObjectMapper objectMapper;

  private PostPaymentRequest validRequest;

  @BeforeEach
  public void setUp() {
    validRequest = new PostPaymentRequest(
      "12345678989987",
      8,
      2025,
      "GBP",
      56,
      "123"
    );
  }

  @Test
  void whenPaymentWithIdExistThenCorrectPaymentIsReturned() throws Exception {
    PostPaymentResponse payment = new PostPaymentResponse();
    payment.setId(UUID.randomUUID());
    payment.setAmount(10);
    payment.setCurrency("USD");
    payment.setStatus(PaymentStatus.AUTHORIZED);
    payment.setExpiryMonth(12);
    payment.setExpiryYear(2024);
    payment.setCardNumberLastFour("4321");

    paymentsRepository.add(payment);

    mvc.perform(MockMvcRequestBuilders.get("/payment/" + payment.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(payment.getStatus().getName()))
        .andExpect(jsonPath("$.cardNumberLastFour").value(payment.getCardNumberLastFour()))
        .andExpect(jsonPath("$.expiryMonth").value(payment.getExpiryMonth()))
        .andExpect(jsonPath("$.expiryYear").value(payment.getExpiryYear()))
        .andExpect(jsonPath("$.currency").value(payment.getCurrency()))
        .andExpect(jsonPath("$.amount").value(payment.getAmount()));
  }

  @Test
  void whenPaymentWithIdDoesNotExistThen404IsReturned() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/payment/" + UUID.randomUUID()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Page not found"));
  }

  @Test
  void testValidRequestPaymentCreation() throws Exception {
    String requestString = objectMapper.writeValueAsString(validRequest);

    String response = mvc.perform(MockMvcRequestBuilders.post(("/payment"))
      .contentType(MediaType.APPLICATION_JSON).content(requestString))
      .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    PostPaymentResponse paymentResponse = objectMapper.readValue(response, PostPaymentResponse.class);
    assertEquals(PaymentStatus.AUTHORIZED, paymentResponse.getStatus());
    assertEquals(validRequest.getCardNumberLastFour(), paymentResponse.getCardNumberLastFour());
    assertEquals(validRequest.getExpiryYear(), paymentResponse.getExpiryYear());
    assertEquals(validRequest.getCurrency(), paymentResponse.getCurrency());
    assertEquals(validRequest.getAmount(), paymentResponse.getAmount());

  }

  @Test
  void testIncorrectCardDeclined() throws Exception {
    validRequest.setCardNumber("12345678989986");
    String requestString = objectMapper.writeValueAsString(validRequest);

    String response = mvc.perform(MockMvcRequestBuilders.post(("/payment"))
    .contentType(MediaType.APPLICATION_JSON).content(requestString))
    .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    PostPaymentResponse responseParsed = objectMapper.readValue(response, PostPaymentResponse.class);
    assertEquals(PaymentStatus.DECLINED, responseParsed.getStatus());
    assertEquals(validRequest.getCardNumberLastFour(), responseParsed.getCardNumberLastFour());
    assertEquals(validRequest.getExpiryYear(), responseParsed.getExpiryYear());
    assertEquals(validRequest.getCurrency(), responseParsed.getCurrency());
    assertEquals(validRequest.getAmount(), responseParsed.getAmount());
  }

  @Test
  void dateInFutureRejected() throws Exception {
    validRequest.setExpiryMonth(1);
    validRequest.setExpiryYear(1999);
    String requestString = objectMapper.writeValueAsString(validRequest);

    String response = mvc.perform(MockMvcRequestBuilders.post(("/payment"))
    .contentType(MediaType.APPLICATION_JSON).content(requestString))
    .andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();

    assertEquals("\"Rejected\"", response);

  }

  @Test
  void testBankUnreachable() {
// TOODO

  }
}
