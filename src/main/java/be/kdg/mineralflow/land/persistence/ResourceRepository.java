package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    Optional<Resource> findByName(String name);
}
