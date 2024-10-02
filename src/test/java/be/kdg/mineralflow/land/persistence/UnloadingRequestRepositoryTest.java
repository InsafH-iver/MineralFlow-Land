package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.testcontainer.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class UnloadingRequestRepositoryTest {

    @Autowired
    private UnloadingRequestRepository unloadingRequestRepository;

    private final PostgreSQLContainer<?> postgreSQLContainer = TestContainerConfig.postgreSQLContainer;

    @BeforeEach
    void setUp() {
        postgreSQLContainer.start();
    }

    @Test
    void addNewUnloadingRequest() {
        //ARRANGE
        String licensePlate = "US-1531";
        ZonedDateTime createdAt = ZonedDateTime.now();
        UnloadingRequest unloadingRequest = new UnloadingRequest("US-1531", createdAt);
        //ACT
        UnloadingRequest savedRequest = unloadingRequestRepository.save(unloadingRequest);
        //ASSERT
        assertThat(savedRequest).isNotNull();
        assertThat(savedRequest.getLicensePlate()).isEqualTo(licensePlate);
    }
}