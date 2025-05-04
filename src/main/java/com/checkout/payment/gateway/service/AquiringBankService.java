package com.checkout.payment.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.checkout.payment.gateway.exception.BankException;
import com.checkout.payment.gateway.model.AquiringBankRequest;
import com.checkout.payment.gateway.model.AquiringBankResponse;

@Service
public class AquiringBankService {

    private final RestTemplate restTemplate;
    private static final Logger LOG = LoggerFactory.getLogger(AquiringBankService.class);

    @Value("${BANK_URL:http://localhost:8080/payments}")
    private final String aquiringBankUrl = null;

    public AquiringBankService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public AquiringBankResponse postToAquiringBank(AquiringBankRequest paymentAquiringBankReq) throws BankException {
        LOG.info("Connecting to bank for with request: " + paymentAquiringBankReq.toString());
        try {
            return restTemplate.postForObject(aquiringBankUrl, paymentAquiringBankReq, AquiringBankResponse.class);
        } catch (RestClientException e) {
            throw new BankException("Error connecting to bank: " + e.getMessage());
        }
       
    }
}
