package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.testcontainer.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UnloadingAppointmentRepositoryTest {

    @Autowired
    private UnloadingAppointmentRepository unloadingAppointmentRepository;
    @Autowired
    private ConfigProperties configProperties;

    private final PostgreSQLContainer<?> postgreSQLContainer = TestContainerConfig.postgreSQLContainer;

    @BeforeEach
    void setUp() {
        postgreSQLContainer.start();
    }

    @Test
    void getUnfulfilledAppointment() {
        //ARRANGE
        String licensePlate = "US-1531";
        ZonedDateTime startTimeSlot = ZonedDateTime.of(2024, 3,
                12, 4, 3, 0, 0, ZoneOffset.UTC);
        UnloadingAppointment unloadingAppointment = new UnloadingAppointment("US-1531", startTimeSlot, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
        unloadingAppointmentRepository.save(unloadingAppointment);
        //ACT
        UnloadingAppointment savedRequest = unloadingAppointmentRepository.getUnfulfilledAppointment(licensePlate);
        //ASSERT
        assertThat(savedRequest).isNotNull();
        assertThat(savedRequest.getLicensePlate()).isEqualTo(licensePlate);
        assertThat(savedRequest.getStartOfTimeSlot()).isEqualTo(startTimeSlot);
    }
}