package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class UnloadingRequestRepositoryTest {

    @Autowired
    private UnloadingRequestRepository unloadingRequestRepository;

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
    void addNewUnloadingRequest() {
        //ARRANGE
        String licensePlate = "US-1531";
        UnloadingRequest unloadingRequest = new UnloadingRequest("US-1531");
        //ACT
        UnloadingRequest savedRequest = unloadingRequestRepository.save(unloadingRequest);
        //ASSERT
        assertThat(savedRequest).isNotNull();
        assertThat(savedRequest.getLicensePlate()).isEqualTo(licensePlate);
    }
}