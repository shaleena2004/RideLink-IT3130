package com.ridelink.ride.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.service.RideService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Passenger can create a ride
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PASSENGER')")
    public RideResponse createRide(
            @Valid @RequestBody RideRequest request) {

        return rideService.createRide(request);
    }

    // Passenger or Driver can view all rides
    @GetMapping
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public List<RideResponse> getAllRides() {

        return rideService.getAllRides();
    }

    // Passenger or Driver can view a specific ride
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public RideResponse getRideById(
            @PathVariable String id) {

        return rideService.getRideById(id);
    }

    // Driver can accept a ride
    @PutMapping("/{id}/accept")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse acceptRide(
            @PathVariable String id,
            @RequestParam String driverId) {

        return rideService.acceptRide(id, driverId);
    }

    // Driver can start a ride
    @PutMapping("/{id}/start")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse startRide(
            @PathVariable String id) {

        return rideService.startRide(id);
    }

    // Driver can complete a ride
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('DRIVER')")
    public RideResponse completeRide(
            @PathVariable String id) {

        return rideService.completeRide(id);
    }

    // Passenger or Driver can cancel a ride
    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('PASSENGER', 'DRIVER')")
    public RideResponse cancelRide(
            @PathVariable String id) {

        return rideService.cancelRide(id);
    }
}