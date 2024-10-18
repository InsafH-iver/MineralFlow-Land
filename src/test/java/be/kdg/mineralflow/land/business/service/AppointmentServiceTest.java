package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseCapacityClient;
import be.kdg.mineralflow.land.config.ConfigProperties;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentServiceTest extends TestContainer {
    @Autowired
    private AppointmentService appointmentService;
    @MockBean
    private WarehouseCapacityClient warehouseCapacityClient;
    @Autowired
    private ConfigProperties configProperties;
    @Test
    void processAppointment_happyPath() {
        //ARRANGE
        UUID resourceId = UUID.fromString("11111111-1111-1111-1111-111111111112");
        UUID vendorId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String licensePlate = "Q-EYX-367";
        ZonedDateTime appointmentDate = ZonedDateTime.of(2024, 11, 23, 10, 9, 32, 8, ZoneId.of("UTC"));
        Mockito.when(warehouseCapacityClient
                .isWarehouseCapacityReached(vendorId, resourceId))
                .thenReturn(Boolean.FALSE);
        //ACT
        UnloadingAppointment unloadingAppointment =
                appointmentService.processAppointment(vendorId, resourceId, licensePlate, appointmentDate);
        //ASSERT
        assertThat(unloadingAppointment).isNotNull();
        assertThat(unloadingAppointment.getStartOfTimeSlot().getHour()).isEqualTo(appointmentDate.getHour());
        assertThat(unloadingAppointment.getLicensePlate()).isEqualTo(licensePlate);
        assertThat(unloadingAppointment.getVisit()).isNull();
        assertThat(unloadingAppointment.getResource().getName()).isEqualTo(resourceId);
    }

    @Test
    void processAppointment_should_make_appointment_on_lower_edge_of_time_for_appointments() {
        //ARRANGE
        String resourceName = "Gips";
        String vendorName = "Acme Supplies";
        String licensePlate = "Q-EYX-367";
        ZonedDateTime appointmentDate = ZonedDateTime.of(2024, 11, 23, configProperties.getStartOfPeriodWithAppointment(), 9, 32, 8, ZoneId.of("UTC"));
        Mockito.when(warehouseCapacityClient
                        .isWarehouseCapacityReached(vendorName, resourceName))
                .thenReturn(Boolean.FALSE);
        //ACT
        UnloadingAppointment unloadingAppointment =
                appointmentService.processAppointment(vendorName, resourceName, licensePlate, appointmentDate);
        //ASSERT
        assertThat(unloadingAppointment).isNotNull();
        assertThat(unloadingAppointment.getStartOfTimeSlot().getHour()).isEqualTo(appointmentDate.getHour());
        assertThat(unloadingAppointment.getLicensePlate()).isEqualTo(licensePlate);
        assertThat(unloadingAppointment.getVisit()).isNull();
        assertThat(unloadingAppointment.getResource().getName()).isEqualTo(resourceName);
    }
    @Test
    void processAppointment_should_throw_validationException_on_upper_edge_of_time_for_appointments() {
        //ARRANGE
        String resourceName = "Gips";
        String vendorName = "Acme Supplies";
        String licensePlate = "Q-EYX-367";
        ZonedDateTime appointmentDate = ZonedDateTime.of(2024, 11, 23, configProperties.getEndOfPeriodWithAppointment(), 0, 0, 0, ZoneId.of("UTC"));
        Mockito.when(warehouseCapacityClient
                        .isWarehouseCapacityReached(vendorName, resourceName))
                .thenReturn(Boolean.FALSE);
        //ACT
        //ASSERT
        assertThrows(ValidationException.class,()->appointmentService.processAppointment(vendorName, resourceName, licensePlate, appointmentDate));
    }
}