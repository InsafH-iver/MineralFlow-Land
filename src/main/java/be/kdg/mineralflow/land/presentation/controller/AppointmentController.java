package be.kdg.mineralflow.land.presentation.controller;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.service.AppointmentService;
import be.kdg.mineralflow.land.presentation.controller.dto.MakeAppointmentDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.time.ZonedDateTime;
import java.util.logging.Logger;

@Controller
public class AppointmentController {
    public static final Logger logger = Logger
            .getLogger(PlanningController.class.getName());
    public final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/createAppointment")
    public ModelAndView createAppointment(@RequestBody MakeAppointmentDto makeAppointmentDto){
        ModelAndView modelAndView = new ModelAndView();
        UnloadingAppointment unloadingAppointment = appointmentService.addAppointment(makeAppointmentDto);
        return modelAndView;
    }
}
