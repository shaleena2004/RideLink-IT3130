package com.ridelink.ride.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.ridelink.ride.dto.FareEstimateRequest;
import com.ridelink.ride.dto.FareEstimateResponse;

@Component
public class FarePaymentServiceClient {

    private final RestClient restClient;

    public FarePaymentServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${ridelink.fare-payment-service.url}") String farePaymentServiceUrl) {

        this.restClient = restClientBuilder
                .baseUrl(farePaymentServiceUrl)
                .build();
    }

    public FareEstimateResponse createFare(
            FareEstimateRequest request,
            String authorizationHeader) {

        return restClient.post()
                .uri("/api/fares")
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(FareEstimateResponse.class);
    }
}