package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.util.TruckArrivalResponse;
import be.kdg.mineralflow.land.config.ConfigLoader;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UnloadingRequestManagerTest {
    @MockBean
    private UnloadingRequestRepository unloadingRequestRepo;
    @MockBean
    private UnloadingAppointmentRepository unloadingAppointmentRepo;
    @Autowired
    private UnloadingRequestManager unloadingRequestManager;

    @Test
    void processTruckArrivalAtGate_Should_Return_UnloadingAppointment_When_arriving_at_beginning_of_Appointment() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeOfArrival = ZonedDateTime.of(2024, 2, 23,
                7, 0, 0, 0,
                ZoneOffset.UTC);
        UnloadingAppointment unloadingAppointment = new UnloadingAppointment(licensePlate, timeOfArrival);

        Mockito.when(unloadingAppointmentRepo.getUnfulfilledAppointment(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doNothing().when(unloadingRequestRepo)
                .createVisitOfUnloadingRequest(unloadingAppointment, timeOfArrival);
        //ACT
        TruckArrivalResponse arrivalResponse = unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertTrue(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(timeOfArrival);
    }

    @Test
    void processTruckArrivalAtGate_Should_Return_UnloadingAppointment_When_Arriving_InBetween_Timeslot_Of_Appointment() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                7, 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime timeOfArrival = timeSlotStart.plusMinutes(28);
        UnloadingAppointment unloadingAppointment = new UnloadingAppointment(licensePlate, timeSlotStart);

        Mockito.when(unloadingAppointmentRepo.getUnfulfilledAppointment(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doNothing().when(unloadingRequestRepo)
                .createVisitOfUnloadingRequest(unloadingAppointment, timeOfArrival);
        //ACT
        TruckArrivalResponse arrivalResponse = unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertTrue(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(timeSlotStart);
    }

    @Test
    void processTruckArrivalAtGate_Should_Return_False_As_GateStatus_When_Arriving_Late() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                7, 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime timeOfArrival = timeSlotStart.plusMinutes(
                ConfigLoader.getProperty(ConfigProperties.DURATION_OF_TIMESLOT_OF_APPOINTMENT_IN_MINUTES) + 8);
        ZonedDateTime startOfPeriodWithoutAppointments = ZonedDateTime.of(2024, 2, 23,
                ConfigLoader.getProperty(ConfigProperties.END_OF_PERIOD_WITH_APPOINTMENT)
                , 0, 0, 0, ZoneOffset.UTC);

        UnloadingAppointment unloadingAppointment = new UnloadingAppointment(licensePlate, timeSlotStart);

        Mockito.when(unloadingAppointmentRepo.getUnfulfilledAppointment(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doNothing().when(unloadingRequestRepo)
                .createVisitOfUnloadingRequest(unloadingAppointment, timeOfArrival);

        //ACT
        TruckArrivalResponse arrivalResponse = unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertFalse(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(startOfPeriodWithoutAppointments);

    }

    @Test
    void processTruckArrivalAtGate_Should_Return_False_As_GateStatus_When_Arriving_At_Beginning_Of_Next_Timeslot() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                7, 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime timeOfArrival = timeSlotStart.plusMinutes(
                ConfigLoader.getProperty(ConfigProperties.DURATION_OF_TIMESLOT_OF_APPOINTMENT_IN_MINUTES)
        );
        UnloadingAppointment unloadingAppointment = new UnloadingAppointment(licensePlate, timeSlotStart);
        ZonedDateTime startOfPeriodWithoutAppointments = ZonedDateTime.of(2024, 2, 23,
                ConfigLoader.getProperty(ConfigProperties.END_OF_PERIOD_WITH_APPOINTMENT)
                , 0, 0, 0,
                ZoneOffset.UTC);

        Mockito.when(unloadingAppointmentRepo.getUnfulfilledAppointment(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doNothing().when(unloadingRequestRepo)
                .createVisitOfUnloadingRequest(unloadingAppointment, timeOfArrival);

        //ACT
        TruckArrivalResponse arrivalResponse = unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertFalse(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(startOfPeriodWithoutAppointments);
    }

    @Test
    void processTruckArrivalAtGate_Should_Return_False_As_GateStatus_When_Arriving_Early() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                7, 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime timeOfArrival = timeSlotStart.minusMinutes(5);
        UnloadingAppointment unloadingAppointment = new UnloadingAppointment(licensePlate, timeSlotStart);

        Mockito.when(unloadingAppointmentRepo.getUnfulfilledAppointment(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doNothing().when(unloadingRequestRepo)
                .createVisitOfUnloadingRequest(unloadingAppointment, timeOfArrival);

        //ACT
        TruckArrivalResponse arrivalResponse = unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertFalse(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(timeSlotStart);
    }

    @Test
    void processTruckArrivalAtGate_Should_Return_False_As_GateStatus_When_No_Appointment() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeOfArrival = ZonedDateTime.of(2024, 2, 23,
                7, 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime startOfPeriodWithoutAppointments = ZonedDateTime.of(2024, 2, 23,
                ConfigLoader.getProperty(ConfigProperties.END_OF_PERIOD_WITH_APPOINTMENT), 0, 0, 0,
                ZoneOffset.UTC);

        Mockito.when(unloadingAppointmentRepo.getUnfulfilledAppointment(licensePlate))
                .thenReturn(null);
        Mockito.doNothing().when(unloadingRequestRepo)
                .createVisitOfUnloadingRequest(null, timeOfArrival);

        //ACT
        TruckArrivalResponse arrivalResponse = unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertFalse(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(startOfPeriodWithoutAppointments);
    }
}