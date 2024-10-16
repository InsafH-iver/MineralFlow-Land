package be.kdg.mineralflow.land.presentation.controller;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.service.UnloadingRequestManager;
import be.kdg.mineralflow.land.presentation.controller.api.UnloadingRequestRestController;
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
            .getLogger(UnloadingRequestRestController.class.getName());
    private final TruckMapper mapper = TruckMapper.INSTANCE;

    private final UnloadingRequestManager unloadingRequestManager;

    public TerrainController( UnloadingRequestManager unloadingRequestManager) {
        this.unloadingRequestManager = unloadingRequestManager;
    }


    @GetMapping("/trucksOnSite")
    public ModelAndView getAllTrucksOnSite(){
        ModelAndView modelAndView = new ModelAndView();
        logger.info("TerrainController: getAllTrucksOnSite has been called");
        List<UnloadingRequest> unloadingRequests = unloadingRequestManager.getUnloadingRequestsWithActiveVisit();
        logger.info(String.format("TerrainController: getAllTrucksOnSite retrieved %s",unloadingRequests));
        List<TruckDto> trucks = unloadingRequests.stream().map(mapper::mapUnloadingRequestToTruckDto).toList();
        modelAndView.addObject(trucks);
        logger.info(modelAndView.getModel().toString());
        return modelAndView;
    }
}
