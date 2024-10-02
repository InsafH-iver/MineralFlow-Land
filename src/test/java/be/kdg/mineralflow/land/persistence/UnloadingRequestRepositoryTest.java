package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class UnloadingRequestRepositoryTest extends TestContainer {

    @Autowired
    private UnloadingRequestRepository unloadingRequestRepository;

    @Test
    void addNewUnloadingRequest() {
        //ARRANGE
        String licensePlate = "US-1531";
        ZonedDateTime createdAt = ZonedDateTime.now();
        UnloadingRequest unloadingRequest = new UnloadingRequest("US-1531", createdAt);
        //ACT
        UnloadingRequest savedRequest = unloadingRequestRepository.save(unloadingRequest);
        //ASSERT
        assertThat(savedRequest).isNotNull();
        assertThat(savedRequest.getLicensePlate()).isEqualTo(licensePlate);
    }
}