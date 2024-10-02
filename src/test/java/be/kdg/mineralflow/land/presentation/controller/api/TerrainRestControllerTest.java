package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.ZonedDateTime;


class TerrainRestControllerTest {

    @Autowired
    private TerrainRestController terrainRestController;
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
    void getAllTrucksOnSite_should_return_all_trucks_that_have_arrivalTime_and_no_leavingTime() {
        //ARRANGE
        String licensePlate = "US-1531";
        ZonedDateTime createdAt = ZonedDateTime.now();
        UnloadingRequest unloadingRequest = new UnloadingRequest(licensePlate,createdAt);
        //ACT
        terrainRestController.getAllTrucksOnSite();
        //ASSERT
        System.out.println(unloadingRequestRepository.findAll());
    }
}