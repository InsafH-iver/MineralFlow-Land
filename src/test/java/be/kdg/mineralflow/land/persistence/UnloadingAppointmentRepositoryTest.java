package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.config.ConfigProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UnloadingAppointmentRepositoryTest extends TestContainer {

    @Autowired
    private UnloadingAppointmentRepository unloadingAppointmentRepository;
    @Autowired
    private ConfigProperties configProperties;

    @Test
    void getUnfulfilledAppointment() {
        //ARRANGE
        String licensePlate = "US-1531";
        ZonedDateTime startTimeSlot = ZonedDateTime.of(2024, 3,
                12, 4, 3, 0, 0, ZoneOffset.UTC);
        UnloadingAppointment unloadingAppointment = new UnloadingAppointment("US-1531", startTimeSlot, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
        unloadingAppointmentRepository.save(unloadingAppointment);
        //ACT
        UnloadingAppointment savedRequest = unloadingAppointmentRepository.findByLicensePlateAndVisitIsNull(licensePlate);
        //ASSERT
        assertThat(savedRequest).isNotNull();
        assertThat(savedRequest.getLicensePlate()).isEqualTo(licensePlate);
        assertThat(savedRequest.getStartOfTimeSlot()).isEqualTo(startTimeSlot);
    }

    @Test
    void unloadingAppointment_timeslot_startOfTimeSlot_and_endOfTimeSlot_should_never_be_null() {
        //ARRANGE
        //ACT
        List<UnloadingAppointment> unloadingAppointments = unloadingAppointmentRepository.findAll();
        //ASSERT
        unloadingAppointments.forEach(unloadingAppointment -> assertThat(unloadingAppointment.getTimeSlot().getStartOfTimeSlot()).isNotNull());
        unloadingAppointments.forEach(unloadingAppointment -> assertThat(unloadingAppointment.getTimeSlot().getEndOfTimeSlot()).isNotNull());
    }
}