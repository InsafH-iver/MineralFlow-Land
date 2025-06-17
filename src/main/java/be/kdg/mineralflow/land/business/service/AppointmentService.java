package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import be.kdg.mineralflow.land.business.domain.warehouse.Vendor;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseCapacityClient;
import be.kdg.mineralflow.land.business.util.ValidationResult;
import be.kdg.mineralflow.land.business.util.provider.ZonedDateTimeProvider;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.persistence.ResourceRepository;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import be.kdg.mineralflow.land.persistence.VendorRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;


@Service
public class AppointmentService {
    public static final Logger logger = Logger
            .getLogger(AppointmentService.class.getName());
    private final UnloadingAppointmentRepository unloadingAppointmentRepository;
    private final ConfigProperties configProperties;
    private final WarehouseCapacityClient warehouseCapacityClient;
    private final ResourceRepository resourceRepository;
    private final VendorRepository vendorRepository;
    private final ZonedDateTimeProvider zonedDateTimeProvider;

    public AppointmentService(UnloadingAppointmentRepository unloadingAppointmentRepository, ConfigProperties configProperties, WarehouseCapacityClient warehouseCapacityClient, ResourceRepository resourceRepository, VendorRepository vendorRepository, ZonedDateTimeProvider zonedDateTimeProvider) {
        this.unloadingAppointmentRepository = unloadingAppointmentRepository;
        this.configProperties = configProperties;
        this.warehouseCapacityClient = warehouseCapacityClient;
        this.resourceRepository = resourceRepository;
        this.vendorRepository = vendorRepository;
        this.zonedDateTimeProvider = zonedDateTimeProvider;
    }

    public UnloadingAppointment processAppointment(String vendorName, String resourceName, String licensePlate, ZonedDateTime appointmentDate) {
        Optional<Resource> optionalResource = resourceRepository.findByName(resourceName);
        Optional<Vendor> optionalVendor = vendorRepository.findByName(vendorName);
        if (optionalVendor.isEmpty() || optionalResource.isEmpty()) throw new NoItemFoundException(String.format("No vendor (%s) or resource (%s) found.",vendorName,resourceName));
        UnloadingAppointment unloadingAppointment =
                new UnloadingAppointment(licensePlate,
                        appointmentDate.truncatedTo(ChronoUnit.HOURS),
                        configProperties.getDurationOfTimeslotOfAppointmentInMinutes(),
                        optionalResource.get(),
                        optionalVendor.get());

        return unloadingAppointmentRepository.saveAndFlush(unloadingAppointment);
    }
    public ValidationResult validateAppointment(String vendorName, String resourceName, ZonedDateTime appointmentDate){
        logger.info(String.format("AppointmentService: Appointment is being validated for vendor: %S, resource: %s, Date: %s",vendorName,resourceName,appointmentDate));
        ValidationResult validationResult = new ValidationResult();
        Optional<Resource> optionalResource = resourceRepository.findByName(resourceName);
        Optional<Vendor> optionalVendor = vendorRepository.findByName(vendorName);
        if (optionalResource.isEmpty()) {
            validationResult.appendMessage(String.format("No resource %s found."
                    , resourceName));
            logger.info(String.format("AppointmentService: Appointment is validated; %s",validationResult.getErrors()));
            return validationResult;
        }
        if (optionalVendor.isEmpty()) {
            validationResult.appendMessage(String.format("No vendor %s found."
                    ,vendorName));
            logger.info(String.format("AppointmentService: Appointment is validated; %s",validationResult.getErrors()));
            return validationResult;
        }
        if (!validateShipment(optionalVendor.get().getId(), optionalResource.get().getId())) {
            validationResult.appendMessage(
                    String.format("Warehouses for this resource (%s) and vendor (%s) are full."
                            , resourceName, vendorName));
        }
        if (!validateAppointmentDate(appointmentDate)) {
            validationResult.appendMessage(
                    String.format("Timeslot at this time (%s) is not available."
                            , appointmentDate));
        }
        logger.info(String.format("AppointmentService: Appointment is validated; %s",validationResult.getErrors()));
        return validationResult;
    }

    private boolean validateAppointmentDate(ZonedDateTime appointmentDate) {
        if (appointmentDate == null) return false;
        if (appointmentDate.isBefore(zonedDateTimeProvider.now(ZoneOffset.UTC))) return false;
        if (!(appointmentDate.getHour() >= configProperties.getStartOfPeriodWithAppointment()
                && appointmentDate.getHour() < configProperties.getEndOfPeriodWithAppointment())) {
            return false;
        }

        int timeslotCount = unloadingAppointmentRepository.countUnloadingRequestsByDateInTimeSlot(appointmentDate);
        return timeslotCount < configProperties.getTruckCapacityDuringAppointments();
    }

    private boolean validateShipment(UUID vendorId, UUID resourceId) {
        return !warehouseCapacityClient.isWarehouseCapacityReached(vendorId, resourceId);
    }
}
