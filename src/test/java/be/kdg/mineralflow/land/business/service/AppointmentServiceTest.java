package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseCapacityClient;
import be.kdg.mineralflow.land.business.util.ValidationResult;
import be.kdg.mineralflow.land.business.util.provider.ZonedDateTimeProvider;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentServiceTest extends TestContainer {
    @Autowired
    private AppointmentService appointmentService;
    @MockBean
    private WarehouseCapacityClient warehouseCapacityClientMock;
    @MockBean
    private UnloadingAppointmentRepository unloadingAppointmentRepositoryMock;
    @Autowired
    private ConfigProperties configProperties;

    @MockBean
    private ZonedDateTimeProvider zonedDateTimeProviderMock;

    @Test
    void processAppointment_happyPath() {
        //ARRANGE
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111112");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String resourceName = "Gips";
        String vendorName = "Acme Supplies";
        String licensePlate = "Q-EYX-367";
        ZonedDateTime appointmentDate = ZonedDateTime.of(2024, 11, 23, 10, 9, 32, 8, ZoneId.of("UTC"));
        ZonedDateTime endTime = ZonedDateTime.of(2024, 10, 23, 10, 9, 32, 8, ZoneId.of("UTC"));

        Mockito.when(zonedDateTimeProviderMock.now(ZoneOffset.UTC)).thenReturn(endTime);
        Mockito.when(warehouseCapacityClientMock
                        .isWarehouseCapacityReached(vendorId, resourceId))
                .thenReturn(Boolean.FALSE);
        ArgumentCaptor<UnloadingAppointment> captor = ArgumentCaptor.forClass(UnloadingAppointment.class);
        Mockito.when(unloadingAppointmentRepositoryMock.saveAndFlush(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //ACT
        ValidationResult validationResult =
                appointmentService.validateAppointment(vendorName, resourceName, appointmentDate);
        UnloadingAppointment unloadingAppointment =
                appointmentService.processAppointment(vendorName, resourceName, licensePlate, appointmentDate);
        //ASSERT
        assertThat(validationResult.getErrors()).isEmpty();
        assertThat(unloadingAppointment).isNotNull();
        assertThat(unloadingAppointment.getStartOfTimeSlot().getHour()).isEqualTo(appointmentDate.getHour());
        assertThat(unloadingAppointment.getLicensePlate()).isEqualTo(licensePlate);
        assertThat(unloadingAppointment.getVisit()).isNull();
        assertThat(unloadingAppointment.getResource().getName()).isEqualTo(resourceName);
    }

    @Test
    void processAppointment_should_make_appointment_on_lower_edge_of_time_for_appointments() {
        //ARRANGE
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111112");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String resourceName = "Gips";
        String vendorName = "Acme Supplies";
        String licensePlate = "Q-EYX-367";
        ZonedDateTime appointmentDate = ZonedDateTime.of(2024, 11, 23, configProperties.getStartOfPeriodWithAppointment(), 9, 32, 8, ZoneId.of("UTC"));
        ZonedDateTime endTime = ZonedDateTime.of(2024, 10, 23, 10, 9, 32, 8, ZoneId.of("UTC"));

        Mockito.when(zonedDateTimeProviderMock.now(ZoneOffset.UTC)).thenReturn(endTime);
        Mockito.when(warehouseCapacityClientMock
                        .isWarehouseCapacityReached(vendorId, resourceId))
                .thenReturn(Boolean.FALSE);
        ArgumentCaptor<UnloadingAppointment> captor = ArgumentCaptor.forClass(UnloadingAppointment.class);
        Mockito.when(unloadingAppointmentRepositoryMock.saveAndFlush(captor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //ACT
        ValidationResult validationResult =
                appointmentService.validateAppointment(vendorName, resourceName, appointmentDate);
        UnloadingAppointment unloadingAppointment =
                appointmentService.processAppointment(vendorName, resourceName, licensePlate, appointmentDate);
        //ASSERT
        assertThat(validationResult.getErrors()).isEmpty();
        assertThat(unloadingAppointment).isNotNull();
        assertThat(unloadingAppointment.getStartOfTimeSlot().getHour()).isEqualTo(appointmentDate.getHour());
        assertThat(unloadingAppointment.getLicensePlate()).isEqualTo(licensePlate);
        assertThat(unloadingAppointment.getVisit()).isNull();
        assertThat(unloadingAppointment.getResource().getName()).isEqualTo(resourceName);
    }

    @Test
    void validateAppointment_should_return_validationResult_with_errors_on_upper_edge_of_time_for_appointments() {
        //ARRANGE
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111112");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String resourceName = "Gips";
        String vendorName = "Acme Supplies";
        ZonedDateTime appointmentDate = ZonedDateTime.of(2024, 11, 23, configProperties.getEndOfPeriodWithAppointment(), 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime endTime = ZonedDateTime.of(2024, 10, 23, 10, 9, 32, 8, ZoneId.of("UTC"));

        Mockito.when(zonedDateTimeProviderMock.now(ZoneOffset.UTC)).thenReturn(endTime);
        Mockito.when(warehouseCapacityClientMock
                        .isWarehouseCapacityReached(vendorId, resourceId))
                .thenReturn(Boolean.FALSE);
        //ACT
        ValidationResult validationResult =
                appointmentService.validateAppointment(vendorName, resourceName, appointmentDate);
        //ASSERT
        assertThat(validationResult.getErrors()).isNotEmpty();
    }

    @Test
    void validateAppointment_should_return_validationResult_with_errors_when_warehouse_is_full() {
        //ARRANGE
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111112");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String resourceName = "Gips";
        String vendorName = "Acme Supplies";
        ZonedDateTime appointmentDate = ZonedDateTime.of(2024, 11, 23, configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime endTime = ZonedDateTime.of(2024, 10, 23, 10, 9, 32, 8, ZoneId.of("UTC"));

        Mockito.when(zonedDateTimeProviderMock.now(ZoneOffset.UTC)).thenReturn(endTime);
        Mockito.when(warehouseCapacityClientMock
                        .isWarehouseCapacityReached(vendorId, resourceId))
                .thenReturn(Boolean.TRUE);
        //ACT
        ValidationResult validationResult =
                appointmentService.validateAppointment(vendorName, resourceName, appointmentDate);
        //ASSERT
        assertThat(validationResult.getErrors()).isNotEmpty();
    }

    @Test
    void validateAppointment_should_return_validationResult_with_errors_when_timeslot_is_full() {
        //ARRANGE
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111112");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String resourceName = "Gips";
        String vendorName = "Acme Supplies";
        ZonedDateTime appointmentDate = ZonedDateTime.of(2024, 11, 23, configProperties.getStartOfPeriodWithAppointment(), 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime endTime = ZonedDateTime.of(2024, 10, 23, 10, 9, 32, 8, ZoneId.of("UTC"));

        Mockito.when(zonedDateTimeProviderMock.now(ZoneOffset.UTC)).thenReturn(endTime);
        Mockito.when(warehouseCapacityClientMock
                        .isWarehouseCapacityReached(vendorId, resourceId))
                .thenReturn(Boolean.FALSE);
        Mockito.when(unloadingAppointmentRepositoryMock.countUnloadingRequestsByDateInTimeSlot(appointmentDate))
                .thenReturn(configProperties.getTruckCapacityDuringAppointments());
        //ACT
        ValidationResult validationResult =
                appointmentService.validateAppointment(vendorName, resourceName, appointmentDate);
        //ASSERT
        assertThat(validationResult.getErrors()).isNotEmpty();
    }
}