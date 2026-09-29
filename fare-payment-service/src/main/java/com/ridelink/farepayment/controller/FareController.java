package com.ridelink.farepayment.controller;

import com.ridelink.farepayment.dto.FareRequest;
import com.ridelink.farepayment.model.Fare;
import com.ridelink.farepayment.service.FareService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fares")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public Fare createFare(
            @Valid @RequestBody FareRequest request) {

        return fareService.createFare(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public List<Fare> getAllFares() {
        return fareService.getAllFares();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'ADMIN')")
    public Fare getFareById(
            @PathVariable String id) {

        return fareService.getFareById(id);
    }
}