package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.Weighbridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WeighbridgeRepository extends JpaRepository<Weighbridge, UUID> {

    @Query("""
            select m
            from Weighbridge m
            order by function('RANDOM')
            """)
    Optional<Weighbridge> findRandomWeighbridge();
}
