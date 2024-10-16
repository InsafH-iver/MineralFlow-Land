package be.kdg.mineralflow.land.presentation.controller;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.service.UnloadingRequestManager;
import be.kdg.mineralflow.land.presentation.controller.dto.PlanningDto;
import be.kdg.mineralflow.land.presentation.controller.mapper.AppointmentMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PlanningController {
    private final UnloadingRequestManager unloadingRequestManager;

    public PlanningController(UnloadingRequestManager unloadingRequestManager) {
        this.unloadingRequestManager = unloadingRequestManager;
    }

    @GetMapping("/planning")
    public ModelAndView getPlanning(){
        ModelAndView modelAndView = new ModelAndView("planning");
        List<UnloadingAppointment> unloadingAppointments = unloadingRequestManager.getAllUnloadingAppointments();
        int queueSize = unloadingRequestManager.getQueueSize();
        PlanningDto planningDto = new PlanningDto(queueSize, AppointmentMapper.INSTANCE.mapAppointments(unloadingAppointments));
        modelAndView.addObject(planningDto);
        return modelAndView;
    }
}
