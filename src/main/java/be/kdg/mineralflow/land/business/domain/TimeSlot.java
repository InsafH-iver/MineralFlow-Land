package be.kdg.mineralflow.land.business.domain;

import java.time.ZonedDateTime;

public class TimeSlot {
    private ZonedDateTime startOfTimeSlot;
    private ZonedDateTime endOfTimeSlot;

    public TimeSlot(ZonedDateTime startOfTimeSlot, int durationOfTimeslotOfAppointmentInMinutes) {
        this.startOfTimeSlot = startOfTimeSlot;
        this.endOfTimeSlot = startOfTimeSlot
                .plusMinutes(durationOfTimeslotOfAppointmentInMinutes);
    }

    protected TimeSlot() {
    }

    public ZonedDateTime getEndOfTimeSlot() {
        return endOfTimeSlot;
    }

    public ZonedDateTime getStartOfTimeSlot() {
        return startOfTimeSlot;
    }
}
