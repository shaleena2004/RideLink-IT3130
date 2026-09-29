package com.ridelink.farepayment.service;

import com.ridelink.farepayment.dto.PaymentRequest;
import com.ridelink.farepayment.model.Payment;
import com.ridelink.farepayment.model.PaymentStatus;
import com.ridelink.farepayment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(PaymentRequest request) {

        Payment payment = new Payment();

        payment.setRideId(request.getRideId());
        payment.setPassengerId(request.getPassengerId());
        payment.setAmount(request.getAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(String id) {

        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Payment not found with id: " + id));
    }

    public Payment processPayment(String id) {

        Payment payment = getPaymentById(id);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING payments can be processed");
        }

        payment.setStatus(PaymentStatus.PAID);

        return paymentRepository.save(payment);
    }

    public Payment failPayment(String id) {

        Payment payment = getPaymentById(id);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only PENDING payments can be failed");
        }

        payment.setStatus(PaymentStatus.FAILED);

        return paymentRepository.save(payment);
    }
}