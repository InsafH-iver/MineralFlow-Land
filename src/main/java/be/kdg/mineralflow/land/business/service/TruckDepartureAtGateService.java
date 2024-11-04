package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.util.ExceptionHandlingHelper;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Service
public class TruckDepartureAtGateService {
    public static final Logger logger = Logger
            .getLogger(TruckDepartureAtGateService.class.getName());
    private final UnloadingRequestRepository unloadingRequestRepository;

    public TruckDepartureAtGateService(UnloadingRequestRepository unloadingRequestRepository) {
        this.unloadingRequestRepository = unloadingRequestRepository;
    }

    public void processTruckDepartureAtGate(String licensePlate, ZonedDateTime timeOfDeparture) {
        logger.info(String.format("Processing departure at gate of truck with license plate" +
                        " %s and time of departure %s",
                licensePlate, timeOfDeparture.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)));
        UnloadingRequest unloadingRequest = getUnloadingRequest(licensePlate);
        setDepartureTimeForUnloadingRequest(unloadingRequest, timeOfDeparture);

        logger.info(String.format("Successfully processed departure at gate of truck with license plate %s"
                , timeOfDeparture.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)));
    }

    private void setDepartureTimeForUnloadingRequest(UnloadingRequest unloadingRequest,
                                                     ZonedDateTime timeOfDeparture) {
        unloadingRequest.setDepartureTime(timeOfDeparture);
        unloadingRequestRepository.save(unloadingRequest);
    }

    private UnloadingRequest getUnloadingRequest(String licensePlate) {
        return unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate)
                .orElseThrow(() -> ExceptionHandlingHelper.logAndThrowNotFound(
                        "No unloading request found where truck with license plate %s needs to depart",
                        licensePlate
                ));
    }
}
