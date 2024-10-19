package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.util.TruckAppointmentArrivalResponse;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UnloadingRequestManagerTest extends TestContainer {
    @MockBean
    private UnloadingRequestRepository unloadingRequestRepo;
    @MockBean
    private UnloadingAppointmentRepository unloadingAppointmentRepo;
    @Autowired
    private UnloadingRequestManager unloadingRequestManager;
    @Autowired
    private ConfigProperties configProperties;

    @Test
    void processTruckArrivalAtGate_Should_Return_UnloadingAppointment_When_arriving_at_beginning_of_Appointment() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeOfArrival = ZonedDateTime.of(2024, 2, 23,
                configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC);
        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate, timeOfArrival, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());

        Mockito.when(unloadingAppointmentRepo.findByLicensePlateAndVisitIsNull(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doReturn(unloadingAppointment).when(unloadingRequestRepo)
                .save(unloadingAppointment);
        //ACT
        TruckAppointmentArrivalResponse arrivalResponse =
                (TruckAppointmentArrivalResponse) unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertTrue(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(timeOfArrival);
    }

    @Test
    void processTruckArrivalAtGate_Should_Return_UnloadingAppointment_When_Arriving_InBetween_Timeslot_Of_Appointment() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime timeOfArrival = timeSlotStart.plusMinutes(28);
        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate, timeSlotStart, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());

        Mockito.when(unloadingAppointmentRepo.findByLicensePlateAndVisitIsNull(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doReturn(unloadingAppointment).when(unloadingRequestRepo)
                .save(unloadingAppointment);
        //ACT
        TruckAppointmentArrivalResponse arrivalResponse =
                (TruckAppointmentArrivalResponse) unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertTrue(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(timeSlotStart);
    }

    @Test
    void processTruckArrivalAtGate_Should_Return_False_As_GateStatus_When_Arriving_Late() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime timeOfArrival = timeSlotStart.plusMinutes(
                configProperties.getDurationOfTimeslotOfAppointmentInMinutes() + 8);
        ZonedDateTime startOfPeriodWithoutAppointments = ZonedDateTime.of(2024, 2, 23,
                configProperties.getEndOfPeriodWithAppointment()
                , 0, 0, 0, ZoneOffset.UTC);

        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate, timeSlotStart, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());

        Mockito.when(unloadingAppointmentRepo.findByLicensePlateAndVisitIsNull(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doReturn(unloadingAppointment).when(unloadingRequestRepo)
                .save(unloadingAppointment);

        //ACT
        TruckAppointmentArrivalResponse arrivalResponse =
                (TruckAppointmentArrivalResponse) unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertFalse(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(startOfPeriodWithoutAppointments);

    }

    @Test
    void processTruckArrivalAtGate_Should_Return_False_As_GateStatus_When_Arriving_At_Beginning_Of_Next_Timeslot() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime timeOfArrival = timeSlotStart.plusMinutes(
                configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate, timeSlotStart, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
        ZonedDateTime startOfPeriodWithoutAppointments = ZonedDateTime.of(2024, 2, 23,
                configProperties.getEndOfPeriodWithAppointment()
                , 0, 0, 0,
                ZoneOffset.UTC);

        Mockito.when(unloadingAppointmentRepo.findByLicensePlateAndVisitIsNull(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doReturn(unloadingAppointment).when(unloadingRequestRepo)
                .save(unloadingAppointment);

        //ACT
        TruckAppointmentArrivalResponse arrivalResponse =
                (TruckAppointmentArrivalResponse) unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertFalse(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(startOfPeriodWithoutAppointments);
    }

    @Test
    void processTruckArrivalAtGate_Should_Return_False_As_GateStatus_When_Arriving_Early() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC).plusMinutes(configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
        ZonedDateTime timeOfArrival = timeSlotStart.minusMinutes(5);
        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate, timeSlotStart, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());

        Mockito.when(unloadingAppointmentRepo.findByLicensePlateAndVisitIsNull(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doReturn(unloadingAppointment).when(unloadingRequestRepo)
                .save(unloadingAppointment);

        //ACT
        TruckAppointmentArrivalResponse arrivalResponse =
                (TruckAppointmentArrivalResponse) unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertFalse(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(timeSlotStart);
    }

    @Test
    void processTruckArrivalAtGate_Should_Return_False_As_GateStatus_When_No_Appointment() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeOfArrival = ZonedDateTime.of(2024, 2, 23,
                configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime startOfPeriodWithoutAppointments = ZonedDateTime.of(2024, 2, 23,
                configProperties.getEndOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC);

        Mockito.when(unloadingAppointmentRepo.findByLicensePlateAndVisitIsNull(licensePlate))
                .thenReturn(null);

        //ACT
        TruckAppointmentArrivalResponse arrivalResponse =
                (TruckAppointmentArrivalResponse) unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        assertFalse(arrivalResponse.gateStatus());
        assertThat(arrivalResponse.returnTimeOfTruck()).isEqualTo(startOfPeriodWithoutAppointments);
    }

    @Test
    void processTruckArrivalAtGate_should_add_unloadingrequest_to_queue_when_truck_is_too_late() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeSlotStart = ZonedDateTime.of(2024, 2, 23,
                configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC);
        ZonedDateTime timeOfArrival = timeSlotStart.plusMinutes(
                configProperties.getDurationOfTimeslotOfAppointmentInMinutes() + 8);

        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate, timeSlotStart, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());

        Mockito.when(unloadingAppointmentRepo.findByLicensePlateAndVisitIsNull(licensePlate))
                .thenReturn(unloadingAppointment);
        Mockito.doReturn(unloadingAppointment).when(unloadingRequestRepo)
                .save(unloadingAppointment);

        //ACT
        unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        ArgumentCaptor<UnloadingRequest> captor = ArgumentCaptor.forClass(UnloadingRequest.class);
        Mockito.verify(unloadingRequestRepo, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue().getLicensePlate()).isEqualTo(licensePlate);
    }

    @Test
    void processTruckArrivalAtGate_should_add_unloadingrequest_to_queue_when_truck_has_no_appointment() {
        //ARRANGE
        String licensePlate = "12345";
        ZonedDateTime timeOfArrival = ZonedDateTime.of(2024, 2, 23,
                configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0,
                ZoneOffset.UTC);

        Mockito.when(unloadingAppointmentRepo.findByLicensePlateAndVisitIsNull(licensePlate))
                .thenReturn(null);

        //ACT
        unloadingRequestManager.processTruckArrivalAtGate(licensePlate, timeOfArrival);

        //ASSERT
        ArgumentCaptor<UnloadingRequest> captor = ArgumentCaptor.forClass(UnloadingRequest.class);
        Mockito.verify(unloadingRequestRepo, Mockito.times(1)).save(captor.capture());
        assertThat(captor.getValue().getLicensePlate()).isEqualTo(licensePlate);
    }
}