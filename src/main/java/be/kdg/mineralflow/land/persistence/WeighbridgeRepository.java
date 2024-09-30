package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.Weighbridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WeighbridgeRepository extends JpaRepository<Weighbridge, UUID> {

    @Query("""
            SELECT w FROM Weighbridge
             w ORDER BY random() LIMIT 1
            """)
    Optional<Weighbridge> findRandomWeighbridge();
}
