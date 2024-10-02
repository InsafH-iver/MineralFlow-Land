package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.persistence.WeighbridgeRepository;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import be.kdg.mineralflow.land.testcontainer.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.ZonedDateTime;
import java.util.List;


@SpringBootTest
class TerrainRestControllerTest {

    @Autowired
    private TerrainRestController terrainRestController;
    @Autowired
    private WeighbridgeRepository weighbridgeRepository;
    private final PostgreSQLContainer<?> postgreSQLContainer = TestContainerConfig.postgreSQLContainer;

    @BeforeEach
    void setUp() {
        postgreSQLContainer.start();
    }

    @Test
    void getAllTrucksOnSite_should_return_all_trucks_that_have_arrivalTime_and_no_leavingTime() {
        //ARRANGE
        String licensePlate = "US-1531";
        ZonedDateTime createdAt = ZonedDateTime.now();
        UnloadingRequest unloadingRequest = new UnloadingRequest(licensePlate,createdAt);
        //ACT
        ResponseEntity<List<TruckDto>> trucks = terrainRestController.getAllTrucksOnSite();
        //ASSERT
        System.out.println("\n"+weighbridgeRepository.findAll()+"nahheaheuah");
    }
}