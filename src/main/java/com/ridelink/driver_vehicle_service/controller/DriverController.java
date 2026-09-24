package com.ridelink.driver_vehicle_service.controller;

import com.ridelink.driver_vehicle_service.dto.DriverRequestDTO;
import com.ridelink.driver_vehicle_service.model.Driver;
import com.ridelink.driver_vehicle_service.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping
    public Driver createDriver(@Valid @RequestBody DriverRequestDTO dto) {

        Driver driver = new Driver();

        driver.setName(dto.getName());
        driver.setPhone(dto.getPhone());
        driver.setLicenseNumber(dto.getLicenseNumber());
        driver.setAvailability(dto.getAvailability());
        driver.setServiceArea(dto.getServiceArea());
        driver.setCurrentLocation(dto.getCurrentLocation());

        return driverService.saveDriver(driver);
    }

    @GetMapping
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{id}")
    public Optional<Driver> getDriverById(@PathVariable String id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}")
    public Driver updateDriver(
            @PathVariable String id,
            @Valid @RequestBody Driver driver) {

        return driverService.updateDriver(id, driver);
    }

    @GetMapping("/available")
    public List<Driver> getAvailableDrivers() {
        return driverService.getAvailableDrivers();
    }
}
