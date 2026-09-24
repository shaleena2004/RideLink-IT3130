package com.ridelink.driver_vehicle_service;

import com.ridelink.driver_vehicle_service.model.Driver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DriverServiceTest {

    @Test
    void testDriverCreation() {

        Driver driver = new Driver();

        driver.setId("1");
        driver.setName("Kamal Perera");
        driver.setPhone("0712345678");
        driver.setLicenseNumber("B123456");
        driver.setAvailability("AVAILABLE");

        assertEquals("Kamal Perera", driver.getName());
        assertEquals("0712345678", driver.getPhone());
        assertEquals("AVAILABLE", driver.getAvailability());
    }
}