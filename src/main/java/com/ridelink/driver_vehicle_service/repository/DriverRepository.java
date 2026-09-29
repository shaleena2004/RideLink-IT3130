package com.ridelink.driver_vehicle_service.repository;

import com.ridelink.driver_vehicle_service.model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends MongoRepository<Driver, String> {

    List<Driver> findByAvailability(String availability);

}
