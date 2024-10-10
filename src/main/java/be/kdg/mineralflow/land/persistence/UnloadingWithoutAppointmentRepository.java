package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnloadingWithoutAppointmentRepository extends JpaRepository<UnloadingWithoutAppointment, UUID> {
}
