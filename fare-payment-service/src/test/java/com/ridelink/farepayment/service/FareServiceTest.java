package com.ridelink.farepayment.service;

import com.ridelink.farepayment.dto.FareRequest;
import com.ridelink.farepayment.model.Fare;
import com.ridelink.farepayment.repository.FareRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FareServiceTest {

    @Mock
    private FareRepository fareRepository;

    @InjectMocks
    private FareService fareService;

    @Test
    void createFare_shouldCalculateAndSaveFare() {

        FareRequest request = new FareRequest();
        request.setRideId("R001");
        request.setPassengerId("P001");
        request.setPickupLocation("Colombo Fort");
        request.setDestination("Kollupitiya");
        request.setDistanceKm(10);

        when(fareRepository.save(any(Fare.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Fare result = fareService.createFare(request);

        assertNotNull(result);
        assertEquals("R001", result.getRideId());
        assertEquals("P001", result.getPassengerId());
        assertEquals("Colombo Fort", result.getPickupLocation());
        assertEquals("Kollupitiya", result.getDestination());

        // Base Fare = 100
        // Price per KM = 50
        // 100 + (10 × 50) = 600
        assertEquals(600.0, result.getEstimatedFare());
        assertEquals(600.0, result.getFinalFare());

        verify(fareRepository, times(1)).save(any(Fare.class));
    }

    @Test
    void getAllFares_shouldReturnAllFares() {

        Fare fare1 = new Fare();
        fare1.setRideId("R001");

        Fare fare2 = new Fare();
        fare2.setRideId("R002");

        when(fareRepository.findAll())
                .thenReturn(List.of(fare1, fare2));

        List<Fare> result = fareService.getAllFares();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("R001", result.get(0).getRideId());
        assertEquals("R002", result.get(1).getRideId());

        verify(fareRepository, times(1)).findAll();
    }

    @Test
    void getFareById_shouldReturnFare_whenFareExists() {

        Fare fare = new Fare();
        fare.setRideId("R001");
        fare.setPassengerId("P001");

        when(fareRepository.findById("fare123"))
                .thenReturn(Optional.of(fare));

        Fare result = fareService.getFareById("fare123");

        assertNotNull(result);
        assertEquals("R001", result.getRideId());
        assertEquals("P001", result.getPassengerId());

        verify(fareRepository, times(1)).findById("fare123");
    }

    @Test
    void getFareById_shouldThrowException_whenFareDoesNotExist() {

        when(fareRepository.findById("invalid-id"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> fareService.getFareById("invalid-id")
        );

        assertEquals(
                "Fare not found with id: invalid-id",
                exception.getMessage()
        );

        verify(fareRepository, times(1)).findById("invalid-id");
    }
}