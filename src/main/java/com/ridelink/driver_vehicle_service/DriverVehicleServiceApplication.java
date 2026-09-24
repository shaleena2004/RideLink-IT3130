package com.ridelink.driver_vehicle_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DriverVehicleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverVehicleServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner testMongo(
            @Value("${spring.data.mongodb.uri}") String uri) {

        return args -> {
            System.out.println("MONGO URI = " + uri);
        };
    }
}
