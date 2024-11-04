package be.kdg.mineralflow.land.presentation.controller;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.service.AppointmentService;
import be.kdg.mineralflow.land.business.util.ValidationResult;
import be.kdg.mineralflow.land.presentation.controller.dto.AppointmentFormDataDto;
import be.kdg.mineralflow.land.presentation.controller.mapper.AppointmentMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
        logger.info("call was made to get appointment form");
        model.addAttribute(new AppointmentFormDataDto());
        return "appointment_form";
    }

    @PostMapping("/appointment")
    public String makeAppointment(AppointmentFormDataDto appointmentFormDataDto, Model model){
        logger.info("call was made make an appointment from form data");
        ValidationResult validationResult =
                appointmentService.validateAppointment(
                appointmentFormDataDto.getVendorName(),
                appointmentFormDataDto.getResourceName(),
                appointmentFormDataDto.getAppointmentDate()
        );
        if (!validationResult.getErrors().isEmpty()){
            model.addAttribute("validationErrors",validationResult.getErrors());
            model.addAttribute(new AppointmentFormDataDto());
            return "appointment_form";
        }
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
