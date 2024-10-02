package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UnloadingAppointmentRepositoryTest {

    @Autowired
    private UnloadingAppointmentRepository unloadingAppointmentRepository;
    @Autowired
    private ConfigProperties configProperties;

    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");


    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
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