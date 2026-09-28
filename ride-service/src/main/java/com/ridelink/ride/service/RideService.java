package com.ridelink.ride.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;

@Service
public class RideService {

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    // Create a new ride request
    public RideResponse createRide(RideRequest request) {

        Ride ride = new Ride();

        ride.setPassengerId(request.getPassengerId());
        ride.setPickupLocation(request.getPickupLocation());
        ride.setDestination(request.getDestination());

        ride.setStatus(RideStatus.REQUESTED);
        ride.setRequestedAt(LocalDateTime.now());

        Ride savedRide = rideRepository.save(ride);

        return toResponse(savedRide);
    }

    // Get all rides
    public List<RideResponse> getAllRides() {

        return rideRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get ride by ID
    public RideResponse getRideById(String id) {

        Ride ride = rideRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride not found with id: " + id));

        return toResponse(ride);
    }

    // Accept a ride
    public RideResponse acceptRide(String id, String driverId) {

        Ride ride = getRideEntityById(id);

        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Ride can only be accepted when status is REQUESTED");
        }

        if (driverId == null || driverId.isBlank()) {
            throw new IllegalArgumentException(
                    "Driver ID is required");
        }

        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);

        return toResponse(updatedRide);
    }

    // Start a ride
    public RideResponse startRide(String id) {

        Ride ride = getRideEntityById(id);

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new IllegalStateException(
                    "Ride can only be started when status is ACCEPTED");
        }

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setStartedAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);

        return toResponse(updatedRide);
    }

    // Complete a ride
    public RideResponse completeRide(String id) {

        Ride ride = getRideEntityById(id);

        if (ride.getStatus() != RideStatus.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Ride can only be completed when status is IN_PROGRESS");
        }

        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);

        return toResponse(updatedRide);
    }

    // Cancel a ride
    public RideResponse cancelRide(String id) {

        Ride ride = getRideEntityById(id);

        if (ride.getStatus() != RideStatus.REQUESTED
                && ride.getStatus() != RideStatus.ACCEPTED) {

            throw new IllegalStateException(
                    "Ride can only be cancelled when status is REQUESTED or ACCEPTED");
        }

        ride.setStatus(RideStatus.CANCELLED);
        ride.setCancelledAt(LocalDateTime.now());

        Ride updatedRide = rideRepository.save(ride);

        return toResponse(updatedRide);
    }

    // Find Ride entity from database
    private Ride getRideEntityById(String id) {

        return rideRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride not found with id: " + id));
    }

    // Convert Ride entity to RideResponse DTO
    private RideResponse toResponse(Ride ride) {

        RideResponse response = new RideResponse();

        response.setId(ride.getId());
        response.setPassengerId(ride.getPassengerId());
        response.setDriverId(ride.getDriverId());
        response.setPickupLocation(ride.getPickupLocation());
        response.setDestination(ride.getDestination());
        response.setStatus(ride.getStatus());

        response.setRequestedAt(ride.getRequestedAt());
        response.setAcceptedAt(ride.getAcceptedAt());
        response.setStartedAt(ride.getStartedAt());
        response.setCompletedAt(ride.getCompletedAt());
        response.setCancelledAt(ride.getCancelledAt());

        return response;
    }
}