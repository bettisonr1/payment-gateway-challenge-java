package com.checkout.payment.gateway.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.service.PaymentGatewayService;
import com.checkout.payment.gateway.service.PaymentValidator;

import jakarta.validation.Valid;

@RestController("api")
public class PaymentGatewayController {
  
  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayController.class);

  private final PaymentGatewayService paymentGatewayService;
  private final PaymentValidator paymentValidator;

  public PaymentGatewayController(PaymentGatewayService paymentGatewayService,
                                  PaymentValidator paymentValidator) {
    this.paymentGatewayService = paymentGatewayService;
    this.paymentValidator = paymentValidator;
  }

  @GetMapping("/payment/{id}")
  public ResponseEntity<PostPaymentResponse> getPostPaymentEventById(@PathVariable UUID id) {
    return new ResponseEntity<>(paymentGatewayService.getPaymentById(id), HttpStatus.OK);
  }

  @PostMapping("/payment")
  public ResponseEntity<PostPaymentResponse> 
    processPayment(@Valid @RequestBody PostPaymentRequest postPaymentRequest, BindingResult bindingResult) {
      LOG.info("Process payment controller entered.");
      if(bindingResult.hasErrors()) {
        paymentValidator.handleErrors(bindingResult, postPaymentRequest);
      }

      paymentValidator.validate(postPaymentRequest);

      return new ResponseEntity<>(paymentGatewayService.processPayment(postPaymentRequest), HttpStatus.OK);
  }
}
