package be.kdg.mineralflow.land.business.domain;

import be.kdg.mineralflow.land.config.ConfigLoader;
import be.kdg.mineralflow.land.config.ConfigProperties;

import java.time.ZonedDateTime;

public class TimeSlot {
    private final ZonedDateTime startOfTimeSlot;
    private final ZonedDateTime endOfTimeSlot;

    public TimeSlot(ZonedDateTime startOfTimeSlot) {
        this.startOfTimeSlot = startOfTimeSlot;
        this.endOfTimeSlot = startOfTimeSlot
             .plusMinutes(ConfigLoader.getProperty(ConfigProperties.DURATION_OF_TIMESLOT_OF_APPOINTMENT_IN_MINUTES));
    }

    public ZonedDateTime getEndOfTimeSlot() {
        return endOfTimeSlot;
    }

    public ZonedDateTime getStartOfTimeSlot() {
        return startOfTimeSlot;
    }
}
