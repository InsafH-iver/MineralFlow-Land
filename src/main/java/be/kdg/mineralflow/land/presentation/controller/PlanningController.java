package be.kdg.mineralflow.land.presentation.controller;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.service.UnloadingRequestService;
import be.kdg.mineralflow.land.presentation.controller.dto.PlanningDto;
import be.kdg.mineralflow.land.presentation.controller.mapper.AppointmentMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class PlanningController {
    public static final Logger logger = Logger
            .getLogger(PlanningController.class.getName());
    private final AppointmentMapper mapper = AppointmentMapper.INSTANCE;

    private final UnloadingRequestService unloadingRequestService;

    public PlanningController(UnloadingRequestService unloadingRequestService) {
        this.unloadingRequestService = unloadingRequestService;
    }

    @GetMapping("/planning")
    public ModelAndView getPlanning(){
        logger.info("TerrainController: getPlanning has been called");
        ModelAndView modelAndView = new ModelAndView("planning");
        List<UnloadingAppointment> unloadingAppointments = unloadingRequestService.getAllUnloadingAppointments();
        int queueSize = unloadingRequestService.getQueueSize();
        PlanningDto planningDto = new PlanningDto(queueSize, mapper.mapAppointments(unloadingAppointments));
        logger.info(String.format("TerrainController: getAllTrucksOnSite retrieved %s",planningDto));
        modelAndView.addObject(planningDto);
        return modelAndView;
    }
}
