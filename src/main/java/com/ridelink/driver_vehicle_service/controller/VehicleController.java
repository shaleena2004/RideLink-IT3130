package com.ridelink.driver_vehicle_service.controller;

import com.ridelink.driver_vehicle_service.model.Vehicle;
import com.ridelink.driver_vehicle_service.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
        return vehicleService.saveVehicle(vehicle);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{id}")
    public Optional<Vehicle> getVehicleById(@PathVariable String id) {
        return vehicleService.getVehicleById(id);
    }

    @PutMapping("/{id}")
    public Vehicle updateVehicle(
            @PathVariable String id,
            @RequestBody Vehicle vehicle) {

        return vehicleService.updateVehicle(id, vehicle);
    }
}
