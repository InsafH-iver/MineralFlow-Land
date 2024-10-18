package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import be.kdg.mineralflow.land.business.domain.warehouse.Vendor;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseCapacityClient;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.ResourceRepository;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import be.kdg.mineralflow.land.persistence.VendorRepository;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;


@Service
public class AppointmentService {
    private final UnloadingAppointmentRepository unloadingAppointmentRepository;
    private final ConfigProperties configProperties;
    private final WarehouseCapacityClient warehouseCapacityClient;
    private final ResourceRepository resourceRepository;
    private final VendorRepository vendorRepository;

    public AppointmentService(UnloadingAppointmentRepository unloadingAppointmentRepository, ConfigProperties configProperties, WarehouseCapacityClient warehouseCapacityClient, ResourceRepository resourceRepository, VendorRepository vendorRepository) {
        this.unloadingAppointmentRepository = unloadingAppointmentRepository;
        this.configProperties = configProperties;
        this.warehouseCapacityClient = warehouseCapacityClient;
        this.resourceRepository = resourceRepository;
        this.vendorRepository = vendorRepository;
    }

    public UnloadingAppointment processAppointment(String vendorName, String resourceName, String licensePlate, ZonedDateTime appointmentDate) {
        Resource resource = resourceRepository.findByName(resourceName);
        if (resource == null) throw new ValidationException(
                String.format("No resource %s found."
                        ,resourceName));

        Vendor vendor = vendorRepository.findByName(vendorName);
        if (vendor == null) throw new ValidationException(
                String.format("No vendor %s found."
                        ,vendorName));
        if (!validateShipment(vendor.getId(), resource.getId())) {
            throw new ValidationException(
                    String.format("Warehouses for this resource (%s) and vendor (%s) are full."
                            , resourceName, vendorName));
        }
        if (!validateAppointmentDate(appointmentDate)) {
            throw new ValidationException(
                    String.format("Timeslot at this time (%s) is not available."
                            , appointmentDate));
        }
        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate,
                        appointmentDate,
                        configProperties.getDurationOfTimeslotOfAppointmentInMinutes(),
                        resource,
                        vendor);
        unloadingAppointment = unloadingAppointmentRepository.saveAndFlush(unloadingAppointment);
        return unloadingAppointment;
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

    private boolean validateShipment(UUID vendorId, UUID resourceId) {
        return !warehouseCapacityClient.isWarehouseCapacityReached(vendorId, resourceId);
    }
}
