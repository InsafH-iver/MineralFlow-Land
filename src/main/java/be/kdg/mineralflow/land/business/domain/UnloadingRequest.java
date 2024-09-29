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
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;
    private String licensePlate;
    private ZonedDateTime createdAt;

    @OneToOne
    private Visit visit;

    public UnloadingRequest(String licensePlate) {
        createdAt = ZonedDateTime.now();
        this.licensePlate = licensePlate;
    }

    protected UnloadingRequest() {
    }

    public boolean hasMatchingLicensePlate(String licensePlate) {
        boolean hasMatchingLicensePlate = this.licensePlate.equals(licensePlate);

        if (hasMatchingLicensePlate) {
            logger.info(
                    String.format("This appointment has been registered for the truck %s", getLicensePlate())
            );
        }

        return hasMatchingLicensePlate;
    }

    public boolean hasNoVisit() {
        boolean hasNoVisit = this.visit == null;

        if (hasNoVisit) {
            logger.info("No truck has arrived for this unloading request");
        }

        return hasNoVisit;
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
