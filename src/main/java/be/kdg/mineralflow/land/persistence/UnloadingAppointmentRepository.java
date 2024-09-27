package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import org.springframework.stereotype.Repository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UnloadingAppointmentRepository {
    private final Map<Integer, UnloadingAppointment> appointments;

    public UnloadingAppointmentRepository() {
        this.appointments = new HashMap<>(Map.of(
                1, new UnloadingAppointment("MOZART",
                        ZonedDateTime.of(2024, 9, 23,
                                9, 0, 0, 0,
                                ZoneOffset.UTC)),
                2, new UnloadingAppointment("BARBIE",
                        ZonedDateTime.of(2024, 9, 25,
                                0, 0, 0, 0,
                                ZoneOffset.UTC)),
                3, new UnloadingAppointment("MOZART",
                        ZonedDateTime.of(2024, 9, 25,
                                9, 0, 0, 0,
                                ZoneOffset.UTC)
                )));
    }


    public UnloadingAppointment getUnfulfilledAppointment(String licensePlate) {
        UnloadingAppointment unloadingAppointment = appointments.values().stream()
                .filter(ua ->
                        ua.hasMatchingLicensePlate(licensePlate)
                                && ua.hasNoVisit())
                .findAny()
                .orElse(null);

        return unloadingAppointment;
    }
}
