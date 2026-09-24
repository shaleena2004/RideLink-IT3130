package com.ridelink.driver_vehicle_service.model;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "drivers")
public class Driver {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "License number is required")
    private String licenseNumber;

    private String availability;
    private String serviceArea;
    private String currentLocation;

    public Driver() {
    }

    public Driver(String id, String name, String phone,
                  String licenseNumber, String availability,
                  String serviceArea, String currentLocation) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.licenseNumber = licenseNumber;
        this.availability = availability;
        this.serviceArea = serviceArea;
        this.currentLocation = currentLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getServiceArea() {
        return serviceArea;
    }

    public void setServiceArea(String serviceArea) {
        this.serviceArea = serviceArea;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }
}
