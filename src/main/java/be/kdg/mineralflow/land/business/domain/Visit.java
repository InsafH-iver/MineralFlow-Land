package be.kdg.mineralflow.land.business.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class Visit {
    public static final Logger logger = Logger
            .getLogger(Visit.class.getName());

    private ZonedDateTime arrivalTime;
    private ZonedDateTime leavingTime;

    private Weighbridge weighBridge;
    private UnloadingRequest unloadingRequest;

    public Visit(ZonedDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public ZonedDateTime getArrivalTime() {
        logger.info(String.format("Fetching arrival  time %s from Visit",
                arrivalTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)));
        return arrivalTime;
    }
}
