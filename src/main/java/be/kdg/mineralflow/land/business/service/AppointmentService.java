package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.TimeSlotRepository;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import org.springframework.stereotype.Service;


@Service
public class AppointmentService {
    private final UnloadingAppointmentRepository unloadingAppointmentRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ConfigProperties configProperties;

    public AppointmentService(UnloadingAppointmentRepository unloadingAppointmentRepository, TimeSlotRepository timeSlotRepository, ConfigProperties configProperties) {
        this.unloadingAppointmentRepository = unloadingAppointmentRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.configProperties = configProperties;
    }


}
