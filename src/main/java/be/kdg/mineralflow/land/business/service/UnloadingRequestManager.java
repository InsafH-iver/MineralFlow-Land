package be.kdg.mineralflow.land.business.service;


import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.util.TruckArrivalResponse;
import be.kdg.mineralflow.land.config.ConfigLoader;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Service
public class UnloadingRequestManager {
    public static final Logger logger = Logger
            .getLogger(UnloadingRequestManager.class.getName());

    private UnloadingAppointmentRepository unloadingAppointmentRepo;
    private UnloadingRequestRepository unloadingRequestRepo;

    public UnloadingRequestManager(UnloadingAppointmentRepository unloadingAppointmentRepo
            , UnloadingRequestRepository unloadingRequestRepo
    ) {
        this.unloadingAppointmentRepo = unloadingAppointmentRepo;
        this.unloadingRequestRepo = unloadingRequestRepo;
    }
    
    public TruckArrivalResponse processTruckArrivalAtGate(String licensePlate, ZonedDateTime timeOfArrival) {
        logger.info(String.format("The truck with license plate %s and arrival time %s is being checked for allowance of entree",
                licensePlate,
                timeOfArrival
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );
        UnloadingAppointment unloadingAppointment = unloadingAppointmentRepo.getUnfulfilledAppointment(licensePlate);

        TruckArrivalResponse arrivalResponse = validateTruckEntry(unloadingAppointment, timeOfArrival, licensePlate);

        if (arrivalResponse.gateStatus()) {
            logger.info(String.format("A visit is being created for the truck with license plate %s and arrival time %s",
                    licensePlate,
                    timeOfArrival
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            unloadingRequestRepo.createVisitOfUnloadingRequest(unloadingAppointment, timeOfArrival);
        }

        return arrivalResponse;
    }

    private TruckArrivalResponse validateTruckEntry(UnloadingAppointment unloadingAppointment,
                                                    ZonedDateTime timeOfArrival, String licensePlate) {

        if (unloadingAppointment == null ||
                !unloadingAppointment.isTruckArrivalEarlyOrOnTime(timeOfArrival)) {
            ZonedDateTime startOfTimeslotWithoutAppointment = ZonedDateTime
                    .of(timeOfArrival.toLocalDate(),
                            LocalTime.of(ConfigLoader.getProperty(ConfigProperties.END_OF_PERIOD_WITH_APPOINTMENT)
                                    , 0, 0), ZoneOffset.UTC);
            logger.info(String.format("The truck with license plate %s has to wait until %s to enter",
                    licensePlate,
                    startOfTimeslotWithoutAppointment.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            return new TruckArrivalResponse(false, startOfTimeslotWithoutAppointment);
        }

        if (unloadingAppointment.isTruckArrivalEarlyForAppointment(timeOfArrival)) {
            logger.info(String.format("The truck with license plate %s has to wait until %s to enter",
                    licensePlate,
                    unloadingAppointment.getStartOfTimeSlot()
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            return new TruckArrivalResponse(false, unloadingAppointment.getStartOfTimeSlot());
        }

        logger.info(String.format("The truck with license plate %s and arrival time %s is allowed to enter",
                licensePlate,
                timeOfArrival
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );
        return new TruckArrivalResponse(true, unloadingAppointment.getStartOfTimeSlot());
    }

}
