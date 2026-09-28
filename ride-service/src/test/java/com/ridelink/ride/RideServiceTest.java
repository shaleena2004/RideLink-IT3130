package com.ridelink.ride;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.RideService;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @InjectMocks
    private RideService rideService;

    private Ride ride;

    @BeforeEach
    void setUp() {
        ride = new Ride();
        ride.setId("ride-001");
        ride.setPassengerId("P001");
        ride.setPickupLocation("Colombo Fort");
        ride.setDestination("Kollupitiya");
        ride.setStatus(RideStatus.REQUESTED);
    }

    @Test
    void createRide_ShouldCreateRequestedRide() {

        RideRequest request = new RideRequest();
        request.setPassengerId("P001");
        request.setPickupLocation("Colombo Fort");
        request.setDestination("Kollupitiya");

        when(rideRepository.save(any(Ride.class)))
                .thenReturn(ride);

        RideResponse response = rideService.createRide(request);

        assertNotNull(response);
        assertEquals("ride-001", response.getId());
        assertEquals("P001", response.getPassengerId());
        assertEquals(RideStatus.REQUESTED, response.getStatus());

        verify(rideRepository, times(1))
                .save(any(Ride.class));
    }

    @Test
    void acceptRide_ShouldChangeStatusToAccepted() {

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response =
                rideService.acceptRide("ride-001", "D001");

        assertEquals(RideStatus.ACCEPTED, response.getStatus());
        assertEquals("D001", response.getDriverId());

        verify(rideRepository, times(1))
                .save(any(Ride.class));
    }

    @Test
    void startRide_ShouldChangeStatusToInProgress() {

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setDriverId("D001");

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response =
                rideService.startRide("ride-001");

        assertEquals(
                RideStatus.IN_PROGRESS,
                response.getStatus()
        );

        verify(rideRepository, times(1))
                .save(any(Ride.class));
    }

    @Test
    void completeRide_ShouldChangeStatusToCompleted() {

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setDriverId("D001");

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response =
                rideService.completeRide("ride-001");

        assertEquals(
                RideStatus.COMPLETED,
                response.getStatus()
        );

        verify(rideRepository, times(1))
                .save(any(Ride.class));
    }

    @Test
    void completeRide_WhenNotInProgress_ShouldThrowException() {

        ride.setStatus(RideStatus.ACCEPTED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        assertThrows(
                IllegalStateException.class,
                () -> rideService.completeRide("ride-001")
        );

        verify(rideRepository, never())
                .save(any(Ride.class));
    }

    @Test
    void cancelRide_ShouldChangeStatusToCancelled() {

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RideResponse response =
                rideService.cancelRide("ride-001");

        assertEquals(
                RideStatus.CANCELLED,
                response.getStatus()
        );

        verify(rideRepository, times(1))
                .save(any(Ride.class));
    }
}