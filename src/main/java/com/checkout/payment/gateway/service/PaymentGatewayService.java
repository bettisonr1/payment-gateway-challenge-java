package com.checkout.payment.gateway.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.BankException;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.AquiringBankResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.utils.PaymentMapper;

@Service
public class PaymentGatewayService {

  private static final Logger LOG = LoggerFactory.getLogger(PaymentGatewayService.class);

  private final PaymentsRepository paymentsRepository;
  private final AquiringBankService aquiringBankService;
  private final PaymentMapper paymentMapper;


  public PaymentGatewayService(PaymentsRepository paymentsRepository,
                              AquiringBankService aquiringBankService,
                              PaymentMapper paymentMapper) {
    this.paymentsRepository = paymentsRepository;
    this.aquiringBankService = aquiringBankService;
    this.paymentMapper = paymentMapper;
  }

  public PostPaymentResponse getPaymentById(UUID id) {
    LOG.debug("Requesting access to to payment with ID {}", id);
    return paymentsRepository.get(id).orElseThrow(() -> new EventProcessingException("Invalid ID"));
  }

  public PostPaymentResponse processPayment(PostPaymentRequest paymentRequest) throws BankException {
    AquiringBankResponse aquiringBankResponse = aquiringBankService
      .postToAquiringBank(paymentMapper.mapToAquiringBankRequest(paymentRequest));

    PostPaymentResponse postPaymentResponse = paymentMapper.mapToPostPaymentResponse(aquiringBankResponse, paymentRequest);
    paymentsRepository.add(postPaymentResponse);

    return postPaymentResponse;
  }

  public PostPaymentResponse reject() {
    PostPaymentResponse rejected = new PostPaymentResponse();
    rejected.setStatus(PaymentStatus.REJECTED);
    return rejected;
  }
}
