package com.ridelink.ride.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ridelink.ride.model.Ride;

public interface RideRepository extends MongoRepository<Ride, String> {
}