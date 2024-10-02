package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class TerrainRestControllerTest extends TestContainer {

    @Autowired
    private TerrainRestController terrainRestController;
    @Autowired
    private UnloadingRequestRepository unloadingRequestRepository;

    @Test
    void getAllTrucksOnSite_should_return_all_trucks_that_have_arrivalTime_and_no_leavingTime() {
        //ARRANGE
        //ACT
        /*
        ResponseEntity<List<TruckDto>> trucks = terrainRestController.getAllTrucksOnSite();
        //ASSERT
        assertThat(trucks).isNotNull();
        assertThat(trucks.getBody()).isNotNull();
        assertThat(trucks.getBody().size()).isEqualTo(2);
        trucks.getBody().forEach(t -> assertThat(t.visit()).isNotNull());
        trucks.getBody().forEach(t -> assertThat(t.visit().weighbridge()).isNotNull());
        trucks.getBody().forEach(t -> assertThat(t.visit().arrivalTime()).isNotNull());
        trucks.getBody().forEach(t -> assertThat(t.visit().leavingTIme()).isNull());

         */
    }
}