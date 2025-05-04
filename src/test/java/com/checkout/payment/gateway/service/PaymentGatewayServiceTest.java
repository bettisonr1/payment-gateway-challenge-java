package com.checkout.payment.gateway.service;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;

import com.checkout.payment.gateway.enums.PaymentStatus;
import com.checkout.payment.gateway.exception.EventProcessingException;
import com.checkout.payment.gateway.model.AquiringBankRequest;
import com.checkout.payment.gateway.model.AquiringBankResponse;
import com.checkout.payment.gateway.model.PostPaymentRequest;
import com.checkout.payment.gateway.model.PostPaymentResponse;
import com.checkout.payment.gateway.repository.PaymentsRepository;
import com.checkout.payment.gateway.utils.PaymentMapper;

@SpringBootTest
public class PaymentGatewayServiceTest {
    
    @Mock 
    PaymentsRepository paymentsRepository;

    @Mock 
    PaymentMapper paymentMapper;

    @Mock
    AquiringBankService aquiringBankService;

    PostPaymentResponse postPaymentResponse = new PostPaymentResponse();
    PostPaymentRequest postPaymentRequest = new PostPaymentRequest();
    AquiringBankRequest aquiringBankRequest = new AquiringBankRequest();
    AquiringBankResponse aquiringBankResponse = new AquiringBankResponse();

    @InjectMocks
    PaymentGatewayService service;

    @Test 
    void testProcessPayment() {
        when(paymentMapper.mapToAquiringBankRequest(postPaymentRequest)).thenReturn(aquiringBankRequest);
        when(aquiringBankService.postToAquiringBank(aquiringBankRequest)).thenReturn(aquiringBankResponse);
        when(paymentMapper.mapToPostPaymentResponse(aquiringBankResponse, postPaymentRequest)).thenReturn(postPaymentResponse);

        PostPaymentResponse response = service.processPayment(postPaymentRequest);

        verify(paymentsRepository, times(1)).add(postPaymentResponse);
        assertEquals(response.getId(), postPaymentResponse.getId());
    }

    @Test
    void testGetPaymentWhenExists() {
        UUID uuid = UUID.randomUUID();
        postPaymentResponse.setId(uuid);
        when(paymentsRepository.get(uuid)).thenReturn(Optional.ofNullable(postPaymentResponse));

        PostPaymentResponse response = service.getPaymentById(uuid);

        assertEquals(response.getId(), postPaymentResponse.getId());
    }

    @Test
    void testGetPaymentWhenItDoesntExists() {
        UUID uuid = UUID.randomUUID();
        postPaymentResponse.setId(uuid);
        when(paymentsRepository.get(uuid)).thenReturn(Optional.ofNullable(null));
        
        assertThrows(EventProcessingException.class, () -> service.getPaymentById(uuid));
    }

    @Test
    void rejectMethodReturnsRejectedPayment() {

        PostPaymentResponse response = service.reject();

        assertEquals(response.getStatus(), PaymentStatus.REJECTED);
    }

}
