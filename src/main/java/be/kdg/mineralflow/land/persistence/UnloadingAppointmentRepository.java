package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface UnloadingAppointmentRepository extends JpaRepository<UnloadingAppointment, UUID> {
    UnloadingAppointment findByLicensePlateAndVisitIsNull(String licensePlate);
}
