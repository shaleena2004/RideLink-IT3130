package com.ridelink.ride.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.ridelink.ride.dto.DriverAvailabilityResponse;

@Component
public class DriverServiceClient {

    private final RestClient restClient;

    public DriverServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${ridelink.driver-service.url}") String driverServiceUrl) {

        this.restClient = restClientBuilder
                .baseUrl(driverServiceUrl)
                .build();
    }

    public List<DriverAvailabilityResponse> getAvailableDrivers(
            String authorizationHeader) {

        DriverAvailabilityResponse[] response =
                restClient.get()
                        .uri("/api/drivers/available")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                authorizationHeader)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(DriverAvailabilityResponse[].class);

        if (response == null) {
            return List.of();
        }

        return Arrays.asList(response);
    }
}