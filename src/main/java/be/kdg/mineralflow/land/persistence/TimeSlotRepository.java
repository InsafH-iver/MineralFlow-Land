package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {
}
