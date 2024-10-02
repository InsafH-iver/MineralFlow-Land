package be.kdg.mineralflow.land.business.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Logger;
import java.time.format.DateTimeFormatter;


@Entity
public class UnloadingRequest {
    public static final Logger logger = Logger
            .getLogger(UnloadingRequest.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String licensePlate;
    private ZonedDateTime createdAt;

    @OneToOne
    private Visit visit;

    public UnloadingRequest(String licensePlate, ZonedDateTime createdAt) {
        this.createdAt = createdAt;
        this.licensePlate = licensePlate;
    }

    protected UnloadingRequest() {
    }

    public void setVisit(Visit visit) {
        logger.info(
                String.format("A truck has arrived at %s for this appointment",
                        visit.getArrivalTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))

        );
        this.visit = visit;
    }

    public String getLicensePlate() {
        logger.info(String.format("Fetching license plate %s from unloading request", licensePlate));
        return licensePlate;
    }

}
