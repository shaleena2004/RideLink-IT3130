package com.ridelink.farepayment.service;

import com.ridelink.farepayment.dto.FareRequest;
import com.ridelink.farepayment.model.Fare;
import com.ridelink.farepayment.repository.FareRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FareService {

    private static final double BASE_FARE = 100.0;
    private static final double PRICE_PER_KM = 50.0;

    private final FareRepository fareRepository;

    public FareService(FareRepository fareRepository) {
        this.fareRepository = fareRepository;
    }

    public Fare createFare(FareRequest request) {

        double estimatedFare =
                BASE_FARE + (request.getDistanceKm() * PRICE_PER_KM);

        double finalFare = estimatedFare;

        Fare fare = new Fare();

        fare.setRideId(request.getRideId());
        fare.setPassengerId(request.getPassengerId());
        fare.setPickupLocation(request.getPickupLocation());
        fare.setDestination(request.getDestination());
        fare.setEstimatedFare(estimatedFare);
        fare.setFinalFare(finalFare);
        fare.setCreatedAt(LocalDateTime.now());

        return fareRepository.save(fare);
    }

    public List<Fare> getAllFares() {
        return fareRepository.findAll();
    }

    public Fare getFareById(String id) {
        return fareRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Fare not found with id: " + id));
    }
}