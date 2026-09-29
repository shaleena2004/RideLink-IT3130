package com.ridelink.farepayment.service;

import com.ridelink.farepayment.model.Payment;
import com.ridelink.farepayment.model.PaymentStatus;
import com.ridelink.farepayment.model.Receipt;
import com.ridelink.farepayment.repository.PaymentRepository;
import com.ridelink.farepayment.repository.ReceiptRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final PaymentRepository paymentRepository;

    public ReceiptService(
            ReceiptRepository receiptRepository,
            PaymentRepository paymentRepository) {

        this.receiptRepository = receiptRepository;
        this.paymentRepository = paymentRepository;
    }

    public Receipt generateReceipt(String paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found with id: " + paymentId));

        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new IllegalStateException(
                    "Receipt can only be generated for PAID payments");
        }

        Receipt receipt = new Receipt();

        receipt.setPaymentId(payment.getId());
        receipt.setRideId(payment.getRideId());
        receipt.setPassengerId(payment.getPassengerId());
        receipt.setAmount(payment.getAmount());
        receipt.setPaymentStatus(payment.getStatus());

        return receiptRepository.save(receipt);
    }

    public List<Receipt> getAllReceipts() {
        return receiptRepository.findAll();
    }

    public Receipt getReceiptById(String id) {

        return receiptRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Receipt not found with id: " + id));
    }
}