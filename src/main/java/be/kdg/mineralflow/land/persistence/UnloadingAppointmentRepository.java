package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;


public interface UnloadingAppointmentRepository extends JpaRepository<UnloadingAppointment, UUID> {

    @Query("""
            select ua
            from UnloadingAppointment ua
            where ua.licensePlate = :licensePlate
            and ua.visit is null
            """)
    UnloadingAppointment getUnfulfilledAppointment(@Param("licensePlate") String licensePlate);

}
