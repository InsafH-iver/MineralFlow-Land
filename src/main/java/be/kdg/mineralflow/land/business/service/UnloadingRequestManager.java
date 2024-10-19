package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import be.kdg.mineralflow.land.business.domain.Visit;
import be.kdg.mineralflow.land.business.util.TruckArrivalResponse;
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
import java.util.Objects;
import java.util.logging.Logger;

@Service
public class UnloadingRequestManager {
    public static final Logger logger = Logger
            .getLogger(UnloadingRequestManager.class.getName());

    private final UnloadingRequestRepository unloadingRequestRepository;
    private final UnloadingAppointmentRepository unloadingAppointmentRepository;
    private final UnloadingWithoutAppointmentRepository unloadingWithoutAppointmentRepository;
    private final ConfigProperties configProperties;

    public UnloadingRequestManager(UnloadingAppointmentRepository unloadingAppointmentRepository
            , UnloadingRequestRepository unloadingRequestRepository, UnloadingWithoutAppointmentRepository unloadingWithoutAppointmentRepository, ConfigProperties configProperties
    ) {
        this.unloadingAppointmentRepository = unloadingAppointmentRepository;
        this.unloadingRequestRepository = unloadingRequestRepository;
        this.unloadingWithoutAppointmentRepository = unloadingWithoutAppointmentRepository;
        this.configProperties = configProperties;
    }

    public TruckArrivalResponse processTruckArrivalAtGate(String licensePlate, ZonedDateTime timeOfArrival) {
        if (timeOfArrival.getHour() >= configProperties.getEndOfPeriodWithAppointment() || timeOfArrival.getHour() < configProperties.getStartOfPeriodWithAppointment()){
            ZonedDateTime startOfTimeSlot = timeOfArrival.truncatedTo(ChronoUnit.HOURS);
            ZonedDateTime endOfTimeSlot = startOfTimeSlot.plusMinutes(configProperties.getDurationOfTimeslotOfAppointmentInMinutes());
            int uwaThisTimeSlot = unloadingWithoutAppointmentRepository.countUnloadingWithoutAppointmentByArrivalTimeInTimeSlot(startOfTimeSlot,endOfTimeSlot);
            if (uwaThisTimeSlot >= configProperties.getTruckCapacityDuringQueue()){
                //needs revision--------------------------------v--------------------------------------------------------------------------------------v
                return new TruckArrivalResponse(false,timeOfArrival.plusMinutes(configProperties.getDurationOfTimeslotOfAppointmentInMinutes()));
            }
            UnloadingWithoutAppointment unloadingWithoutAppointment = unloadingWithoutAppointmentRepository.getFirstInQueue();
            if (Objects.equals(licensePlate, unloadingWithoutAppointment.getLicensePlate())){
                return new TruckArrivalResponse(true, timeOfArrival);
            }
        }


        logger.info(String.format("The truck with license plate %s and arrival time %s is being checked for allowance of entree",
                licensePlate,
                timeOfArrival
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );
        UnloadingAppointment unloadingAppointment = unloadingAppointmentRepository.findByLicensePlateAndVisitIsNull(licensePlate);

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
        UnloadingWithoutAppointment queueEntry = new UnloadingWithoutAppointment(licensePlate, createdAt);
        logger.info(String.format("New unloadingRequest %s is being saved.", queueEntry));
        UnloadingRequest saved = unloadingRequestRepository.save(queueEntry);
        logger.info(String.format("UnloadingRequest %s was saved succesfully.", saved));
    }

    public List<UnloadingRequest> getUnloadingRequestsWithActiveVisit() {
        logger.info("UnloadingRequestManager: getUnloadingRequestsWithActiveVisit has been called");
        return unloadingRequestRepository.readUnloadingRequestsWithActiveVisit();
    }

    public List<UnloadingAppointment> getAllUnloadingAppointments() {
        logger.info("UnloadingRequestManager: getAllUnloadingAppointments has been called");
        return unloadingAppointmentRepository.findAll();
    }

    public int getQueueSize() {
        return unloadingWithoutAppointmentRepository.findAllByVisitIsNull().size();
    }
}
