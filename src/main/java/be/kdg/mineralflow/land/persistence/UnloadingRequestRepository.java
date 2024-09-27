package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.Visit;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UnloadingRequestRepository {
    private final Map<Integer, UnloadingRequest> appointments;

    public UnloadingRequestRepository() {
        this.appointments = new HashMap<>(Map.of(
                1, new UnloadingRequest("MOZART"),
                2, new UnloadingRequest("BARBIE"),
                3, new UnloadingRequest("CABLE")
        ));
    }

    public void createVisitOfUnloadingRequest(UnloadingRequest unloadingRequest, ZonedDateTime timeOfArrival) {
        Visit visit = new Visit(timeOfArrival);
        unloadingRequest.setVisit(visit);
    }
}
