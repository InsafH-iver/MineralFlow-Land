package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.TimeSlotRepository;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;


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

    public UnloadingAppointment addAppointment(String licensePlate, String resourceName, ZonedDateTime timeOfAppointment) {
        UnloadingAppointment unloadingAppointment = new UnloadingAppointment(licensePlate,timeOfAppointment
                .truncatedTo(ChronoUnit.HOURS),configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
        if (validateAppointment(unloadingAppointment)){
            unloadingAppointmentRepository.save(unloadingAppointment);
        }
    }
    private boolean validateAppointment(UnloadingAppointment unloadingAppointment){

    }

}
