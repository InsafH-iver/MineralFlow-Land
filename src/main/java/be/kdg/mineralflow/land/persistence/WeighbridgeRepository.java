package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.Weighbridge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WeighbridgeRepository extends JpaRepository<Weighbridge, UUID> {
    Optional<Weighbridge> findTopByOrderByWeighbridgeNumber();
}
