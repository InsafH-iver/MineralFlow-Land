package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.presentation.controller.dto.PlaceHolderDto;
import be.kdg.mineralflow.land.presentation.controller.dto.PlanningDto;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UnloadingRequestRestControllerTest extends TestContainer {

    @Autowired
    private UnloadingRequestRestController unloadingRequestRestController;
    @Test
    void getPlanning() {

        ResponseEntity<PlanningDto> response = unloadingRequestRestController.getPlanning();
        //ASSERT
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().queue()).isNotNull();
        assertThat(response.getBody().appointments()).isNotNull();
        assertThat(response.getBody().queue().size()).isEqualTo(5);
        assertThat(response.getBody().appointments().size()).isEqualTo(5);
        response.getBody().appointments().forEach(appointmentDto -> assertThat(appointmentDto.timeslotDto()).isNotNull());
        response.getBody().appointments().forEach(appointmentDto -> assertThat(appointmentDto.timeslotDto().startOfTimeslot()).isNotNull());
        response.getBody().appointments().forEach(appointmentDto -> assertThat(appointmentDto.timeslotDto().endOfTimeslot()).isNotNull());
    }
}