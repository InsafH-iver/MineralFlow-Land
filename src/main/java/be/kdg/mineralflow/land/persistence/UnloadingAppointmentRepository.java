package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.UUID;


public interface UnloadingAppointmentRepository extends JpaRepository<UnloadingAppointment, UUID> {
    UnloadingAppointment findByLicensePlateAndVisitIsNull(String licensePlate);

    @Query(
            "SELECT COUNT(u) FROM UnloadingAppointment u " +
                    "WHERE :date BETWEEN u.timeSlot.startOfTimeSlot AND u.timeSlot.endOfTimeSlot"
    )
    int countUnloadingRequestsByDateInTimeSlot(@Param("date") ZonedDateTime date);}
