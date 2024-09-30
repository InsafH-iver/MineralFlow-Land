package be.kdg.mineralflow.land.business.domain;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Entity
public class UnloadingAppointment extends UnloadingRequest {
    public static final Logger logger = Logger
            .getLogger(UnloadingAppointment.class.getName());


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "startOfTimeSlot", column = @Column(name = "start_of_timeslot")),
            @AttributeOverride(name = "endOfTimeSlot", column = @Column(name = "end_of_timeslot"))
    })
    private TimeSlot timeSlot;

    public UnloadingAppointment(String licensePlate, ZonedDateTime startOfTimeSlot) {
        super(licensePlate, null);
        this.timeSlot = new TimeSlot(startOfTimeSlot);
    }

    protected UnloadingAppointment() {
    }

    public ZonedDateTime getStartOfTimeSlot() {
        logger.info(String.format("Fetching Start of time slot: %s", timeSlot));
        return timeSlot.getStartOfTimeSlot();
    }

    public boolean isTruckArrivalEarlyOrOnTime(ZonedDateTime timeOfArrival) {
        boolean isOnTime = timeOfArrival.isBefore(timeSlot.getEndOfTimeSlot());

        if (isOnTime) {
            logger.info(
                    String.format(
                            "The truck %s arrived Early or on time. Time of arrival: %s, Start of timeslot: %s",
                            getLicensePlate(),
                            timeOfArrival.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                            timeSlot.getStartOfTimeSlot().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
        } else {
            logger.info(String.format("The truck %s arrived late. Time of arrival: %s, Start of timeslot: %s",
                    getLicensePlate(),
                    timeOfArrival.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                    timeSlot.getStartOfTimeSlot().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
        }
        return isOnTime;
    }

    public boolean isTruckArrivalEarlyForAppointment(ZonedDateTime timeOfArrival) {
        boolean isEarly = timeOfArrival.isBefore(timeSlot.getStartOfTimeSlot());

        if (isEarly) {
            logger.info(
                    String.format(
                            "The truck %s arrived Early or on time. Time of arrival: %s, Start of timeslot: %s",
                            getLicensePlate(),
                            timeOfArrival.format(DateTimeFormatter.ISO_ZONED_DATE_TIME),
                            timeSlot.getStartOfTimeSlot().format(DateTimeFormatter.ISO_ZONED_DATE_TIME))
            );
        }

        return isEarly;
    }
}
