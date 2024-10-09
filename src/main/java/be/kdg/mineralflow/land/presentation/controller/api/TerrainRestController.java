package be.kdg.mineralflow.land.presentation.controller.api;


import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.service.UnloadingRequestManager;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import be.kdg.mineralflow.land.presentation.controller.mapper.TruckMapper;
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
    private final TruckMapper mapper = TruckMapper.INSTANCE;

    private final UnloadingRequestManager unloadingRequestManager;

    public TerrainRestController( UnloadingRequestManager unloadingRequestManager) {
        this.unloadingRequestManager = unloadingRequestManager;
    }


    @GetMapping("/AllTrucksOnSite")
    public ResponseEntity<List<TruckDto>> getAllTrucksOnSite(){
        logger.info("TerrainRestController: getAllTrucksOnSite has been called");
        List<UnloadingRequest> unloadingRequests = unloadingRequestManager.getUnloadingRequestsWithActiveVisit();
        logger.info(String.format("TerrainRestController: getAllTrucksOnSite retrieved %s",unloadingRequests));
        List<TruckDto> trucks = unloadingRequests.stream().map(mapper::mapUnloadingRequestToTruckDto).toList();
        return ResponseEntity.ok(trucks);
    }

}
