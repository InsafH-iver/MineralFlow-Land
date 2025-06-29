package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.service.TruckDepartureAtGateService;
import be.kdg.mineralflow.land.business.service.UnloadingRequestService;
import be.kdg.mineralflow.land.business.util.provider.ZonedDateTimeProvider;
import be.kdg.mineralflow.land.business.util.response.TruckArrivalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final TruckDepartureAtGateService truckDepartureAtGateService;
    private final ZonedDateTimeProvider zonedDateTimeProvider;

    public UnloadingRequestRestController(UnloadingRequestService unloadingRequestService, TruckDepartureAtGateService truckDepartureAtGateService, ZonedDateTimeProvider zonedDateTimeProvider) {
        this.unloadingRequestService = unloadingRequestService;
        this.truckDepartureAtGateService = truckDepartureAtGateService;
        this.zonedDateTimeProvider = zonedDateTimeProvider;
    }

    @PostMapping(value = "/visit/{licensePlate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TruckArrivalResponse> processTruckArrivalAtGate(@PathVariable String licensePlate) {
        ZonedDateTime timeOfArrival = zonedDateTimeProvider.now(ZoneOffset.UTC);

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

    @PatchMapping(value = "/departure/{licensePlate}")
    @ResponseStatus(HttpStatus.OK)
    public void processTruckDepartureAtGate(@PathVariable String licensePlate) {
        ZonedDateTime timeOfDeparture = zonedDateTimeProvider.now(ZoneOffset.UTC);

        logger.info(String.format("A call has been made to process the departure for truck with license plate %s and departure time %s",
                licensePlate,
                timeOfDeparture.format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
        );

        truckDepartureAtGateService.processTruckDepartureAtGate(licensePlate, timeOfDeparture);
    }

}
