package com.ridelink.farepayment.controller;

import com.ridelink.farepayment.model.Receipt;
import com.ridelink.farepayment.service.ReceiptService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/payment/{paymentId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public Receipt generateReceipt(
            @PathVariable String paymentId) {

        return receiptService.generateReceipt(paymentId);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public List<Receipt> getAllReceipts() {
        return receiptService.getAllReceipts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public Receipt getReceiptById(
            @PathVariable String id) {

        return receiptService.getReceiptById(id);
    }
}