package com.ridelink.farepayment.service;

import com.ridelink.farepayment.model.Payment;
import com.ridelink.farepayment.model.PaymentStatus;
import com.ridelink.farepayment.model.Receipt;
import com.ridelink.farepayment.repository.PaymentRepository;
import com.ridelink.farepayment.repository.ReceiptRepository;
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
class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private ReceiptService receiptService;

    @Test
    void generateReceipt_shouldCreateReceipt_whenPaymentIsPaid() {

        Payment payment = new Payment();
        payment.setId("payment123");
        payment.setRideId("R001");
        payment.setPassengerId("P001");
        payment.setAmount(600.0);
        payment.setStatus(PaymentStatus.PAID);

        when(paymentRepository.findById("payment123"))
                .thenReturn(Optional.of(payment));

        when(receiptRepository.save(any(Receipt.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Receipt result = receiptService.generateReceipt("payment123");

        assertNotNull(result);
        assertEquals("payment123", result.getPaymentId());
        assertEquals("R001", result.getRideId());
        assertEquals("P001", result.getPassengerId());
        assertEquals(600.0, result.getAmount());
        assertEquals(PaymentStatus.PAID, result.getPaymentStatus());
        assertNotNull(result.getIssuedAt());

        verify(paymentRepository, times(1)).findById("payment123");
        verify(receiptRepository, times(1)).save(any(Receipt.class));
    }

    @Test
    void generateReceipt_shouldThrowException_whenPaymentDoesNotExist() {

        when(paymentRepository.findById("invalid-payment"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receiptService.generateReceipt("invalid-payment")
        );

        assertEquals(
                "Payment not found with id: invalid-payment",
                exception.getMessage()
        );

        verify(paymentRepository, times(1)).findById("invalid-payment");
        verify(receiptRepository, never()).save(any(Receipt.class));
    }

    @Test
    void generateReceipt_shouldThrowException_whenPaymentIsNotPaid() {

        Payment payment = new Payment();
        payment.setId("payment123");
        payment.setRideId("R001");
        payment.setPassengerId("P001");
        payment.setAmount(600.0);
        payment.setStatus(PaymentStatus.PENDING);

        when(paymentRepository.findById("payment123"))
                .thenReturn(Optional.of(payment));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> receiptService.generateReceipt("payment123")
        );

        assertEquals(
                "Receipt can only be generated for PAID payments",
                exception.getMessage()
        );

        verify(paymentRepository, times(1)).findById("payment123");
        verify(receiptRepository, never()).save(any(Receipt.class));
    }

    @Test
    void getAllReceipts_shouldReturnAllReceipts() {

        Receipt receipt1 = new Receipt();
        receipt1.setPaymentId("payment001");

        Receipt receipt2 = new Receipt();
        receipt2.setPaymentId("payment002");

        when(receiptRepository.findAll())
                .thenReturn(List.of(receipt1, receipt2));

        List<Receipt> result = receiptService.getAllReceipts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("payment001", result.get(0).getPaymentId());
        assertEquals("payment002", result.get(1).getPaymentId());

        verify(receiptRepository, times(1)).findAll();
    }

    @Test
    void getReceiptById_shouldReturnReceipt_whenReceiptExists() {

        Receipt receipt = new Receipt();
        receipt.setPaymentId("payment123");
        receipt.setRideId("R001");
        receipt.setPassengerId("P001");
        receipt.setAmount(600.0);
        receipt.setPaymentStatus(PaymentStatus.PAID);

        when(receiptRepository.findById("receipt123"))
                .thenReturn(Optional.of(receipt));

        Receipt result = receiptService.getReceiptById("receipt123");

        assertNotNull(result);
        assertEquals("payment123", result.getPaymentId());
        assertEquals("R001", result.getRideId());
        assertEquals("P001", result.getPassengerId());
        assertEquals(600.0, result.getAmount());
        assertEquals(PaymentStatus.PAID, result.getPaymentStatus());

        verify(receiptRepository, times(1)).findById("receipt123");
    }

    @Test
    void getReceiptById_shouldThrowException_whenReceiptDoesNotExist() {

        when(receiptRepository.findById("invalid-receipt"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> receiptService.getReceiptById("invalid-receipt")
        );

        assertEquals(
                "Receipt not found with id: invalid-receipt",
                exception.getMessage()
        );

        verify(receiptRepository, times(1)).findById("invalid-receipt");
    }
}