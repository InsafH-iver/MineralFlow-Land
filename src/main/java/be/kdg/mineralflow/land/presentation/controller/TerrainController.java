package be.kdg.mineralflow.land.presentation.controller;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.service.UnloadingRequestManager;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckDto;
import be.kdg.mineralflow.land.presentation.controller.mapper.TruckMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class TerrainController {

    public static final Logger logger = Logger
            .getLogger(TerrainController.class.getName());

    private final TruckMapper mapper = TruckMapper.INSTANCE;

    private final UnloadingRequestManager unloadingRequestManager;

    public TerrainController( UnloadingRequestManager unloadingRequestManager) {
        this.unloadingRequestManager = unloadingRequestManager;
    }


    @GetMapping("/trucksOnSite")
    public ModelAndView getAllTrucksOnSite(){
        logger.info("TerrainController: getAllTrucksOnSite has been called");
        ModelAndView modelAndView = new ModelAndView();
        List<UnloadingRequest> unloadingRequests = unloadingRequestManager.getUnloadingRequestsWithActiveVisit();
        List<TruckDto> trucks = unloadingRequests.stream().map(mapper::mapUnloadingRequestToTruckDto).toList();
        logger.info(String.format("TerrainController: getAllTrucksOnSite retrieved %s",trucks));
        modelAndView.addObject(trucks);
        return modelAndView;
    }
}
