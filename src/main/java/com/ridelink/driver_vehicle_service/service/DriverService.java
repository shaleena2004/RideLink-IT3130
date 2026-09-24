package com.ridelink.driver_vehicle_service.service;

import com.ridelink.driver_vehicle_service.model.Driver;
import com.ridelink.driver_vehicle_service.repository.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    public Driver saveDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<Driver> getDriverById(String id) {
        return driverRepository.findById(id);
    }

    public Driver updateDriver(String id, Driver updatedDriver) {

        Driver driver = driverRepository.findById(id).orElse(null);

        if (driver != null) {
            driver.setName(updatedDriver.getName());
            driver.setPhone(updatedDriver.getPhone());
            driver.setLicenseNumber(updatedDriver.getLicenseNumber());
            driver.setAvailability(updatedDriver.getAvailability());
            driver.setServiceArea(updatedDriver.getServiceArea());
            driver.setCurrentLocation(updatedDriver.getCurrentLocation());

            return driverRepository.save(driver);
        }

        return null;
    }

    public List<Driver> getAvailableDrivers() {
        return driverRepository.findByAvailability("AVAILABLE");
    }
}
