package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseCapacityClient;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.ResourceRepository;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;


@Service
public class AppointmentService {
    private final UnloadingAppointmentRepository unloadingAppointmentRepository;
    private final ConfigProperties configProperties;
    private final WarehouseCapacityClient warehouseCapacityClient;
    private final ResourceRepository resourceRepository;

    public AppointmentService(UnloadingAppointmentRepository unloadingAppointmentRepository, ConfigProperties configProperties, WarehouseCapacityClient warehouseCapacityClient, ResourceRepository resourceRepository) {
        this.unloadingAppointmentRepository = unloadingAppointmentRepository;
        this.configProperties = configProperties;
        this.warehouseCapacityClient = warehouseCapacityClient;
        this.resourceRepository = resourceRepository;
    }

    public UnloadingAppointment processAppointment(String vendorName, String resourceName, String licensePlate, ZonedDateTime appointmentDate) {
        if (!validateShipment(vendorName, resourceName)) {
            throw new ValidationException(
                    String.format("Warehouses for this resource (%s) and vendor (%s) are full."
                            , resourceName, vendorName));
        }
        if (!validateAppointmentDate(appointmentDate)) {
            throw new ValidationException(
                    String.format("Timeslot at this time (%s) is not available."
                            , appointmentDate));
        }

        Resource resource = resourceRepository.findByName(resourceName);
        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate,
                        appointmentDate,
                        configProperties.getDurationOfTimeslotOfAppointmentInMinutes(),
                        resource);
        return unloadingAppointmentRepository.save(unloadingAppointment);
    }

    private boolean validateAppointmentDate(ZonedDateTime appointmentDate) {
        if (appointmentDate == null) return false;
        if (appointmentDate.isBefore(ZonedDateTime.now())) return false;
        if (!(appointmentDate.getHour() >= configProperties.getStartOfPeriodWithAppointment()
                && appointmentDate.getHour() < configProperties.getEndOfPeriodWithAppointment())) {
            return false;
        }

        int timeslotCount = unloadingAppointmentRepository.countUnloadingRequestsByDateInTimeSlot(appointmentDate);
        return timeslotCount < configProperties.getTruckCapacity();
    }

    private boolean validateShipment(String vendorName, String resourceName) {
        return !warehouseCapacityClient.isWarehouseCapacityReached(vendorName, resourceName);
    }
}
