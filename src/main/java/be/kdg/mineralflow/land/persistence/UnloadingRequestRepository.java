package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnloadingRequestRepository extends JpaRepository<UnloadingRequest, UUID> {

}
