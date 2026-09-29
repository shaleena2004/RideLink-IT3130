package com.ridelink.ride;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ridelink.ride.dto.DriverAvailabilityResponse;
import com.ridelink.ride.dto.RideRequest;
import com.ridelink.ride.dto.RideResponse;
import com.ridelink.ride.model.Ride;
import com.ridelink.ride.model.RideStatus;
import com.ridelink.ride.repository.RideRepository;
import com.ridelink.ride.service.DriverServiceClient;
import com.ridelink.ride.service.RideService;

@ExtendWith(MockitoExtension.class)
class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverServiceClient driverServiceClient;

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
    void createRide_shouldCreateRequestedRide() {

        RideRequest request = new RideRequest();

        request.setPassengerId("P001");
        request.setPickupLocation("Colombo Fort");
        request.setDestination("Kollupitiya");

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation -> {

                    Ride savedRide = invocation.getArgument(0);
                    savedRide.setId("ride-001");

                    return savedRide;
                });

        RideResponse response = rideService.createRide(request);

        assertNotNull(response);
        assertEquals("ride-001", response.getId());
        assertEquals("P001", response.getPassengerId());
        assertEquals("Colombo Fort", response.getPickupLocation());
        assertEquals("Kollupitiya", response.getDestination());
        assertEquals(RideStatus.REQUESTED, response.getStatus());

        verify(rideRepository).save(any(Ride.class));
    }

    @Test
    void getAllRides_shouldReturnAllRides() {

        when(rideRepository.findAll())
                .thenReturn(List.of(ride));

        List<RideResponse> responses =
                rideService.getAllRides();

        assertEquals(1, responses.size());
        assertEquals("ride-001", responses.get(0).getId());
        assertEquals(RideStatus.REQUESTED,
                responses.get(0).getStatus());

        verify(rideRepository).findAll();
    }

    @Test
    void getRideById_shouldReturnRide() {

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        RideResponse response =
                rideService.getRideById("ride-001");

        assertNotNull(response);
        assertEquals("ride-001", response.getId());
        assertEquals("P001", response.getPassengerId());

        verify(rideRepository).findById("ride-001");
    }

    @Test
    void getRideById_shouldThrowExceptionWhenRideNotFound() {

        when(rideRepository.findById("unknown"))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> rideService.getRideById("unknown"));

        assertEquals(
                "Ride not found with id: unknown",
                exception.getMessage());
    }

    @Test
    void acceptRide_shouldAcceptAvailableDriver() {

        String authorizationHeader =
                "Bearer test-jwt-token";

        DriverAvailabilityResponse driver =
                new DriverAvailabilityResponse();

        driver.setId("D001");
        driver.setName("Test Driver");
        driver.setAvailability("AVAILABLE");

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(driverServiceClient.getAvailableDrivers(
                authorizationHeader))
                .thenReturn(List.of(driver));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        RideResponse response =
                rideService.acceptRide(
                        "ride-001",
                        "D001",
                        authorizationHeader);

        assertNotNull(response);
        assertEquals("D001", response.getDriverId());
        assertEquals(
                RideStatus.ACCEPTED,
                response.getStatus());

        assertNotNull(response.getAcceptedAt());

        verify(driverServiceClient)
                .getAvailableDrivers(authorizationHeader);

        verify(rideRepository)
                .save(any(Ride.class));
    }

    @Test
    void acceptRide_shouldRejectUnavailableDriver() {

        String authorizationHeader =
                "Bearer test-jwt-token";

        DriverAvailabilityResponse driver =
                new DriverAvailabilityResponse();

        driver.setId("D001");
        driver.setName("Test Driver");
        driver.setAvailability("BUSY");

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(driverServiceClient.getAvailableDrivers(
                authorizationHeader))
                .thenReturn(List.of(driver));

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> rideService.acceptRide(
                                "ride-001",
                                "D001",
                                authorizationHeader));

        assertEquals(
                "Driver is not available",
                exception.getMessage());

        verify(rideRepository, never())
                .save(any(Ride.class));
    }

    @Test
    void startRide_shouldChangeStatusToInProgress() {

        ride.setStatus(RideStatus.ACCEPTED);
        ride.setDriverId("D001");

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        RideResponse response =
                rideService.startRide("ride-001");

        assertEquals(
                RideStatus.IN_PROGRESS,
                response.getStatus());

        assertNotNull(response.getStartedAt());

        verify(rideRepository)
                .save(any(Ride.class));
    }

    @Test
    void completeRide_shouldChangeStatusToCompleted() {

        ride.setStatus(RideStatus.IN_PROGRESS);
        ride.setDriverId("D001");

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        RideResponse response =
                rideService.completeRide("ride-001");

        assertEquals(
                RideStatus.COMPLETED,
                response.getStatus());

        assertNotNull(response.getCompletedAt());

        verify(rideRepository)
                .save(any(Ride.class));
    }

    @Test
    void cancelRide_shouldCancelRequestedRide() {

        ride.setStatus(RideStatus.REQUESTED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        when(rideRepository.save(any(Ride.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        RideResponse response =
                rideService.cancelRide("ride-001");

        assertEquals(
                RideStatus.CANCELLED,
                response.getStatus());

        assertNotNull(response.getCancelledAt());

        verify(rideRepository)
                .save(any(Ride.class));
    }

    @Test
    void startRide_shouldRejectRequestedRide() {

        ride.setStatus(RideStatus.REQUESTED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> rideService.startRide("ride-001"));

        assertEquals(
                "Ride can only be started when status is ACCEPTED",
                exception.getMessage());

        verify(rideRepository, never())
                .save(any(Ride.class));
    }

    @Test
    void completeRide_shouldRejectAcceptedRide() {

        ride.setStatus(RideStatus.ACCEPTED);

        when(rideRepository.findById("ride-001"))
                .thenReturn(Optional.of(ride));

        IllegalStateException exception =
                assertThrows(
                        IllegalStateException.class,
                        () -> rideService.completeRide("ride-001"));

        assertEquals(
                "Ride can only be completed when status is IN_PROGRESS",
                exception.getMessage());

        verify(rideRepository, never())
                .save(any(Ride.class));
    }
}