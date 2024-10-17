package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseCapacityClient;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;


@Service
public class AppointmentService {
    private final UnloadingAppointmentRepository unloadingAppointmentRepository;
    private final ConfigProperties configProperties;
    private final WarehouseCapacityClient warehouseCapacityClient;

    public AppointmentService(UnloadingAppointmentRepository unloadingAppointmentRepository, ConfigProperties configProperties, WarehouseCapacityClient warehouseCapacityClient) {
        this.unloadingAppointmentRepository = unloadingAppointmentRepository;
        this.configProperties = configProperties;
        this.warehouseCapacityClient = warehouseCapacityClient;
    }

    public UnloadingAppointment processAppointment(String vendorName, String resourceName, String licensePlate, ZonedDateTime appointmentDate) {
        if (validateShipment(vendorName,resourceName)) return null;
        if (validateAppointmentDate(appointmentDate)) return null;
        UnloadingAppointment unloadingAppointment = new UnloadingAppointment(licensePlate, appointmentDate, configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
        return unloadingAppointmentRepository.save(unloadingAppointment);
    }

    private boolean validateAppointmentDate(ZonedDateTime appointmentDate) {
        int timeslotCount = unloadingAppointmentRepository.countUnloadingRequestsByDateInTimeSlot(appointmentDate);
       return timeslotCount < configProperties.getTruckCapacity();
    }

    private boolean validateShipment(String vendorName, String resourceName) {
        return warehouseCapacityClient.isWarehouseCapacityReached(vendorName, resourceName);
    }
}
