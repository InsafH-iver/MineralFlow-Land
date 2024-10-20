package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

public interface UnloadingWithoutAppointmentRepository extends JpaRepository<UnloadingWithoutAppointment, UUID> {

    List<UnloadingWithoutAppointment> findAllByVisitIsNull();

    @Query("SELECT u FROM UnloadingWithoutAppointment u WHERE u.visit IS NULL ORDER BY u.createdAt LIMIT 1")
    UnloadingWithoutAppointment getFirstInQueue();


    @Query("SELECT count(u) FROM UnloadingWithoutAppointment u " +
            "WHERE u.visit IS NOT NULL AND u.visit.arrivalTime " +
            "BETWEEN :startOfTimeSlot AND :endOfTimeSlot")
    int countUnloadingWithoutAppointmentByArrivalTimeInTimeSlot(ZonedDateTime startOfTimeSlot, ZonedDateTime endOfTimeSlot);
}
