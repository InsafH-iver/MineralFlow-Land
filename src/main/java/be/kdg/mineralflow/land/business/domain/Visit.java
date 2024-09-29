package be.kdg.mineralflow.land.business.domain;

import jakarta.persistence.*;

import java.util.UUID;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Entity
public class Visit {

    public static final Logger logger = Logger
            .getLogger(Visit.class.getName());
    @Id
    private UUID Id;
    private ZonedDateTime arrivalTime;
    private ZonedDateTime leavingTime;

    @ManyToOne
    private Weighbridge weighBridge;
    @OneToOne
    private UnloadingRequest unloadingRequest;
    @OneToOne
    private WeighbridgeTicket weighbridgeTicket;

    public Visit(ZonedDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public ZonedDateTime getArrivalTime() {
        logger.info(String.format("Fetching arrival  time %s from Visit",
                arrivalTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME)));
        return arrivalTime;
    }
}
