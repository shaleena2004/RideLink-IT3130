package com.ridelink.driver_vehicle_service.repository;

import com.ridelink.driver_vehicle_service.model.Vehicle;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends MongoRepository<Vehicle, String> {

}
