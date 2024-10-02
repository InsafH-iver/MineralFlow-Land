package be.kdg.mineralflow.land.presentation.controller.api;


import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.service.UnloadingRequestManager;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/Terrain")
public class TerrainRestController {
    public static final Logger logger = Logger
            .getLogger(UnloadingRequestRestController.class.getName());
    private static ModelMapper modelMapper;

    private final UnloadingRequestManager unloadingRequestManager;

    public TerrainRestController(UnloadingRequestManager unloadingRequestManager) {
        this.unloadingRequestManager = unloadingRequestManager;
    }


    @GetMapping("/AllTrucksOnSite")
    public ResponseEntity<List<TruckDto>> getAllTrucksOnSite(){
        List<UnloadingRequest> unloadingRequests = unloadingRequestManager.getUnloadingRequestsWithActiveVisit();
        List<TruckDto> trucks = unloadingRequests.stream().map(ur -> modelMapper.map(ur, TruckDto.class)).toList();
        return ResponseEntity.ok(trucks);
    }

}
