package com.ridelink.driver_vehicle_service.controller;

import com.ridelink.driver_vehicle_service.dto.DriverRequestDTO;
import com.ridelink.driver_vehicle_service.model.Driver;
import com.ridelink.driver_vehicle_service.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DRIVER')")
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
    @PreAuthorize("hasAnyRole('ADMIN','DRIVER')")
    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DRIVER')")
    public Optional<Driver> getDriverById(@PathVariable String id) {
        return driverService.getDriverById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DRIVER')")
    public Driver updateDriver(
            @PathVariable String id,
            @Valid @RequestBody Driver driver) {

        return driverService.updateDriver(id, driver);
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('ADMIN','DRIVER')")
    public List<Driver> getAvailableDrivers() {
        return driverService.getAvailableDrivers();
    }
}
