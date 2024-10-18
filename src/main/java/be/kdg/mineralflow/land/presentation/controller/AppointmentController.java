package be.kdg.mineralflow.land.presentation.controller;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.service.AppointmentService;
import be.kdg.mineralflow.land.presentation.controller.dto.AppointmentFormDataDto;
import be.kdg.mineralflow.land.presentation.controller.mapper.AppointmentMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@Controller
public class AppointmentController {
    public static final Logger logger = Logger
            .getLogger(AppointmentController.class.getName());
    public final AppointmentService appointmentService;
    public final AppointmentMapper mapper = AppointmentMapper.INSTANCE;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @GetMapping("/appointment")
    public String getAppointmentView(Model model){
        model.addAttribute(new AppointmentFormDataDto());
        return "/makeAppointment";
    }

    @PostMapping("/makeAppointment")
    public String makeAppointment(AppointmentFormDataDto appointmentFormDataDto, Model model){
        UnloadingAppointment unloadingAppointment =
                appointmentService.processAppointment(
                        appointmentFormDataDto.getVendorName(),
                        appointmentFormDataDto.getResourceName(),
                        appointmentFormDataDto.getLicensePlate(),
                        appointmentFormDataDto.getAppointmentDate());
        model.addAttribute(mapper.mapUnloadingAppointmentToAppointmentFormDataDto(unloadingAppointment));
        return "/appointment";
    }
}
