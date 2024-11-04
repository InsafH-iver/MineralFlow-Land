package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import be.kdg.mineralflow.land.business.util.response.TruckAppointmentArrivalResponse;
import be.kdg.mineralflow.land.business.util.response.TruckArrivalResponse;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.persistence.UnloadingAppointmentRepository;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import be.kdg.mineralflow.land.persistence.UnloadingWithoutAppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

@Service
public class UnloadingRequestService {
    public static final Logger logger = Logger
            .getLogger(UnloadingRequestService.class.getName());

    private final UnloadingRequestRepository unloadingRequestRepository;
    private final UnloadingAppointmentRepository unloadingAppointmentRepository;
    private final UnloadingWithoutAppointmentRepository unloadingWithoutAppointmentRepository;
    private final ConfigProperties configProperties;

    public UnloadingRequestService(UnloadingAppointmentRepository unloadingAppointmentRepository
            , UnloadingRequestRepository unloadingRequestRepository, UnloadingWithoutAppointmentRepository unloadingWithoutAppointmentRepository, ConfigProperties configProperties
    ) {
        this.unloadingAppointmentRepository = unloadingAppointmentRepository;
        this.unloadingRequestRepository = unloadingRequestRepository;
        this.unloadingWithoutAppointmentRepository = unloadingWithoutAppointmentRepository;
        this.configProperties = configProperties;
    }

    public TruckArrivalResponse processTruckArrivalAtGate(String licensePlate, ZonedDateTime timeOfArrival) {
        logger.info(String.format("The truck with license plate %s and arrival time %s is being checked for allowance of entree",
                licensePlate,
                timeOfArrival
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );
        if (timeOfArrival.getHour() >= configProperties.getEndOfPeriodWithAppointment() ||
                timeOfArrival.getHour() < configProperties.getStartOfPeriodWithAppointment()) {
            return processQueue(licensePlate, timeOfArrival);
        }

        UnloadingAppointment unloadingAppointment = unloadingAppointmentRepository.findByLicensePlateAndVisitIsNull(licensePlate);

        TruckAppointmentArrivalResponse arrivalResponse = validateTruckEntry(unloadingAppointment, timeOfArrival, licensePlate);

        if (arrivalResponse.gateStatus()) {
            logger.info(String.format("A visit is being created for the truck with license plate %s and arrival time %s",
                    licensePlate,
                    timeOfArrival
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            addVisitToUnloadingRequest(unloadingAppointment, timeOfArrival);
        }

        return arrivalResponse;
    }

    private TruckArrivalResponse processQueue(String licensePlate, ZonedDateTime timeOfArrival) {
        ZonedDateTime startOfTimeSlot = timeOfArrival.truncatedTo(ChronoUnit.HOURS);
        ZonedDateTime endOfTimeSlot = startOfTimeSlot.plusMinutes(configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
        int amountEnteredThisTimeSlot = unloadingWithoutAppointmentRepository.countUnloadingWithoutAppointmentByArrivalTimeInTimeSlot(startOfTimeSlot, endOfTimeSlot);

        if (amountEnteredThisTimeSlot >= configProperties.getTruckCapacityDuringQueue()) {
            return new TruckArrivalResponse(false);
        }
        UnloadingWithoutAppointment firstInQueue = unloadingWithoutAppointmentRepository.getFirstInQueue();

        if (firstInQueue.getLicensePlate().equals(licensePlate)) {
            addVisitToUnloadingRequest(firstInQueue, timeOfArrival);
            return new TruckArrivalResponse(true);
        }
        return new TruckArrivalResponse(false);
    }

    private void addVisitToUnloadingRequest(UnloadingRequest unloadingRequest, ZonedDateTime timeOfArrival) {
        unloadingRequest.addNewVisit(timeOfArrival);
        unloadingRequestRepository.save(unloadingRequest);
    }

    private TruckAppointmentArrivalResponse validateTruckEntry(UnloadingAppointment unloadingAppointment,
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
            return new TruckAppointmentArrivalResponse(false, startOfTimeslotWithoutAppointment);
        }

        if (unloadingAppointment.isTruckArrivalEarlyForAppointment(timeOfArrival)) {
            logger.info(String.format("The truck with license plate %s has to wait until %s to enter",
                    licensePlate,
                    unloadingAppointment.getStartOfTimeSlot()
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            return new TruckAppointmentArrivalResponse(false, unloadingAppointment.getStartOfTimeSlot());
        }

        logger.info(String.format("The truck with license plate %s and arrival time %s is allowed to enter",
                licensePlate,
                timeOfArrival.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );
        return new TruckAppointmentArrivalResponse(true, unloadingAppointment.getStartOfTimeSlot());
    }

    private void addUnloadingRequestToQueue(String licensePlate, ZonedDateTime createdAt) {
        UnloadingWithoutAppointment queueEntry = new UnloadingWithoutAppointment(licensePlate, createdAt);
        logger.info(String.format("New unloadingRequest %s is being saved.", queueEntry));
        UnloadingRequest saved = unloadingRequestRepository.save(queueEntry);
        logger.info(String.format("UnloadingRequest %s was saved succesfully.", saved));
    }

    public List<UnloadingRequest> getUnloadingRequestsWithActiveVisit() {
        logger.info("getUnloadingRequestsWithActiveVisit has been called");
        return unloadingRequestRepository.readUnloadingRequestsWithActiveVisit();
    }

    public List<UnloadingAppointment> getAllUnloadingAppointments() {
        logger.info("getAllUnloadingAppointments has been called");
        return unloadingAppointmentRepository.findAll();
    }

    public int getQueueSize() {
        return unloadingWithoutAppointmentRepository.findAllByVisitIsNull().size();
    }
}
