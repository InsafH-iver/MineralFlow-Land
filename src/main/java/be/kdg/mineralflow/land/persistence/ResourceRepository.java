package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    Resource findByName(String name);
}
