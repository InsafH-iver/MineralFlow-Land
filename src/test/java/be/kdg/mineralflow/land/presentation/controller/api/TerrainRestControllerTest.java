package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Stream;


class TerrainRestControllerTest {

    @Autowired
    private TerrainRestController terrainRestController;
    private UnloadingRequestRepository unloadingRequestRepository;
    @Test
    void getAllTrucksOnSite_should_return_all_trucks_that_have_arrivalTime_and_no_leavingTime() {
        //ARRANGE
        String licensePlate = "US-1531";
        ZonedDateTime createdAt = ZonedDateTime.now();
        UnloadingRequest unloadingRequest = new UnloadingRequest(licensePlate,createdAt);
        unloadingRequestRepository.save()
        //ACT
        terrainRestController.getAllTrucksOnSite();
        //ASSERT
    }
}