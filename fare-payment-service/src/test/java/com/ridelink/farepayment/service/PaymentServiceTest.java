package com.ridelink.farepayment.service;

import com.ridelink.farepayment.dto.PaymentRequest;
import com.ridelink.farepayment.model.Payment;
import com.ridelink.farepayment.model.PaymentStatus;
import com.ridelink.farepayment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void createPayment_shouldCreatePendingPayment() {

        PaymentRequest request = new PaymentRequest();
        request.setRideId("R001");
        request.setPassengerId("P001");
        request.setAmount(600.0);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.createPayment(request);

        assertNotNull(result);
        assertEquals("R001", result.getRideId());
        assertEquals("P001", result.getPassengerId());
        assertEquals(600.0, result.getAmount());
        assertEquals(PaymentStatus.PENDING, result.getStatus());
        assertNotNull(result.getPaymentDate());

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void getAllPayments_shouldReturnAllPayments() {

        Payment payment1 = new Payment();
        payment1.setRideId("R001");

        Payment payment2 = new Payment();
        payment2.setRideId("R002");

        when(paymentRepository.findAll())
                .thenReturn(List.of(payment1, payment2));

        List<Payment> result = paymentService.getAllPayments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("R001", result.get(0).getRideId());
        assertEquals("R002", result.get(1).getRideId());

        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void getPaymentById_shouldReturnPayment_whenPaymentExists() {

        Payment payment = new Payment();
        payment.setRideId("R001");
        payment.setPassengerId("P001");
        payment.setAmount(600.0);

        when(paymentRepository.findById("payment123"))
                .thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentById("payment123");

        assertNotNull(result);
        assertEquals("R001", result.getRideId());
        assertEquals("P001", result.getPassengerId());
        assertEquals(600.0, result.getAmount());

        verify(paymentRepository, times(1)).findById("payment123");
    }

    @Test
    void getPaymentById_shouldThrowException_whenPaymentDoesNotExist() {

        when(paymentRepository.findById("invalid-id"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentService.getPaymentById("invalid-id")
        );

        assertEquals(
                "Payment not found with id: invalid-id",
                exception.getMessage()
        );

        verify(paymentRepository, times(1)).findById("invalid-id");
    }

    @Test
    void processPayment_shouldChangePendingToPaid() {

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById("payment123"))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.processPayment("payment123");

        assertNotNull(result);
        assertEquals(PaymentStatus.PAID, result.getStatus());

        verify(paymentRepository, times(1)).findById("payment123");
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void failPayment_shouldChangePendingToFailed() {

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById("payment123"))
                .thenReturn(Optional.of(payment));

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.failPayment("payment123");

        assertNotNull(result);
        assertEquals(PaymentStatus.FAILED, result.getStatus());

        verify(paymentRepository, times(1)).findById("payment123");
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void processPayment_shouldThrowException_whenPaymentIsNotPending() {

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findById("payment123"))
                .thenReturn(Optional.of(payment));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> paymentService.processPayment("payment123")
        );

        assertEquals(
                "Only PENDING payments can be processed",
                exception.getMessage()
        );

        verify(paymentRepository, times(1)).findById("payment123");
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void failPayment_shouldThrowException_whenPaymentIsNotPending() {

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.FAILED);

        when(paymentRepository.findById("payment123"))
                .thenReturn(Optional.of(payment));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> paymentService.failPayment("payment123")
        );

        assertEquals(
                "Only PENDING payments can be failed",
                exception.getMessage()
        );

        verify(paymentRepository, times(1)).findById("payment123");
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}