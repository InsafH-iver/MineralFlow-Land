package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.Visit;
import be.kdg.mineralflow.land.business.util.TruckArrivalResponse;
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

    private final UnloadingRequestRepository unloadingRequestRepository;
    private final UnloadingAppointmentRepository unloadingAppointmentRepository;
    private final ConfigProperties configProperties;

    public UnloadingRequestManager(UnloadingAppointmentRepository unloadingAppointmentRepository
            , UnloadingRequestRepository unloadingRequestRepository, ConfigProperties configProperties
    ) {
        this.unloadingAppointmentRepository = unloadingAppointmentRepository;
        this.unloadingRequestRepository = unloadingRequestRepository;
        this.configProperties = configProperties;
    }

    public TruckArrivalResponse processTruckArrivalAtGate(String licensePlate, ZonedDateTime timeOfArrival) {
        logger.info(String.format("The truck with license plate %s and arrival time %s is being checked for allowance of entree",
                licensePlate,
                timeOfArrival
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );
        UnloadingAppointment unloadingAppointment = unloadingAppointmentRepository.getUnfulfilledAppointment(licensePlate);

        TruckArrivalResponse arrivalResponse = validateTruckEntry(unloadingAppointment, timeOfArrival, licensePlate);

        if (arrivalResponse.gateStatus()) {
            logger.info(String.format("A visit is being created for the truck with license plate %s and arrival time %s",
                    licensePlate,
                    timeOfArrival
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            addVisitToUnloadingAppointment(unloadingAppointment, timeOfArrival);
        }

        return arrivalResponse;
    }

    private void addVisitToUnloadingAppointment(UnloadingRequest unloadingRequest, ZonedDateTime timeOfArrival) {
        unloadingRequest.setVisit(new Visit(timeOfArrival));
        unloadingRequestRepository.save(unloadingRequest);
    }

    private TruckArrivalResponse validateTruckEntry(UnloadingAppointment unloadingAppointment,
                                                    ZonedDateTime timeOfArrival, String licensePlate) {

        if (unloadingAppointment == null ||
                !unloadingAppointment.isTruckArrivalEarlyOrOnTime(timeOfArrival)) {
            ZonedDateTime startOfTimeslotWithoutAppointment = ZonedDateTime
                    .of(timeOfArrival.toLocalDate(),
                            LocalTime.of(configProperties.getEndOfPeriodWithAppointment()
                                    , 0, 0), ZoneOffset.UTC);
            logger.info(String.format("The truck with license plate %s has to wait until %s to enter",
                    licensePlate,
                    startOfTimeslotWithoutAppointment.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            addUnloadingRequestToQueue(licensePlate, timeOfArrival);
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

    private void addUnloadingRequestToQueue(String licensePlate, ZonedDateTime createdAt) {
        UnloadingRequest unloadingRequest = new UnloadingRequest(licensePlate, createdAt);
        logger.info(String.format("New unloadingRequest %s is being saved.", unloadingRequest));
        UnloadingRequest saved = unloadingRequestRepository.save(unloadingRequest);
        logger.info(String.format("UnloadingRequest %s was saved succesfully.", saved));
    }
}
