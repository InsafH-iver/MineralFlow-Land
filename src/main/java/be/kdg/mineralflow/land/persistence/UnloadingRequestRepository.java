package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface UnloadingRequestRepository extends JpaRepository<UnloadingRequest, UUID> {

    @Query(
            "select u from UnloadingRequest u " +
            "join u.visit v " +
            "where v.leavingTime is null " +
            "and v.arrivalTime is not null"
    )
    List<UnloadingRequest> readUnloadingRequestsWithActiveVisit();

}
