package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.service.UnloadingRequestService;
import be.kdg.mineralflow.land.business.util.response.TruckArrivalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/UnloadingRequests")
public class UnloadingRequestRestController {
    public static final Logger logger = Logger
            .getLogger(UnloadingRequestRestController.class.getName());

    private final UnloadingRequestService unloadingRequestService;

    public UnloadingRequestRestController(UnloadingRequestService unloadingRequestService) {
        this.unloadingRequestService = unloadingRequestService;
    }

    @PostMapping(value ="/visit/{licensePlate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TruckArrivalResponse> processTruckArrivalAtGate(@PathVariable String licensePlate) {
        ZonedDateTime timeOfArrival = ZonedDateTime.now(ZoneOffset.UTC);

        logger.info(String.format("A call has been made to process the truck with license plate %s and arrival time %s",
                licensePlate,
                timeOfArrival
                        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );
        TruckArrivalResponse arrivalResponse = unloadingRequestService
                .processTruckArrivalAtGate(licensePlate, timeOfArrival);


        if (arrivalResponse.gateStatus()) {
            logger.info(String.format("The truck with license plate %s and arrival time %s has been granted entrance and has a visit",
                    licensePlate,
                    timeOfArrival
                            .format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(arrivalResponse);
        } else {// kan veranderen vanaf dat het fifo gedeelte erbij komt
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(arrivalResponse);
        }
    }

}
