package com.ridelink.farepayment.controller;

import com.ridelink.farepayment.dto.PaymentRequest;
import com.ridelink.farepayment.model.Payment;
import com.ridelink.farepayment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public Payment createPayment(
            @Valid @RequestBody PaymentRequest request) {

        return paymentService.createPayment(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public List<Payment> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public Payment getPaymentById(
            @PathVariable String id) {

        return paymentService.getPaymentById(id);
    }

    @PutMapping("/{id}/process")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public Payment processPayment(
            @PathVariable String id) {

        return paymentService.processPayment(id);
    }

    @PutMapping("/{id}/fail")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public Payment failPayment(
            @PathVariable String id) {

        return paymentService.failPayment(id);
    }
}