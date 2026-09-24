package com.ridelink.driver_vehicle_service.service;

import com.ridelink.driver_vehicle_service.model.Vehicle;
import com.ridelink.driver_vehicle_service.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> getVehicleById(String id) {
        return vehicleRepository.findById(id);
    }

    public Vehicle updateVehicle(String id, Vehicle updatedVehicle) {

        Vehicle vehicle = vehicleRepository.findById(id).orElse(null);

        if (vehicle != null) {

            vehicle.setDriverId(updatedVehicle.getDriverId());
            vehicle.setRegistrationNumber(updatedVehicle.getRegistrationNumber());
            vehicle.setVehicleType(updatedVehicle.getVehicleType());
            vehicle.setModel(updatedVehicle.getModel());
            vehicle.setColor(updatedVehicle.getColor());

            return vehicleRepository.save(vehicle);
        }

        return null;
    }
}
