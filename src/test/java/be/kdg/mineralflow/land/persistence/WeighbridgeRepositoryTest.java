package be.kdg.mineralflow.land.persistence;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.Weighbridge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class WeighbridgeRepositoryTest extends TestContainer {

    @Autowired
    private WeighbridgeRepository weighbridgeRepository;
    @Test
    void findRandomWeighbridge() {
        //ARRANGE
        weighbridgeRepository.save(new Weighbridge(1));
        weighbridgeRepository.save(new Weighbridge(2));
        weighbridgeRepository.save(new Weighbridge(3));
        //ACT
        Optional<Weighbridge> optionalWeighbridge = weighbridgeRepository.findTopByOrderByWeighbridgeNumber();
        //ASSERT
        assertTrue(optionalWeighbridge.isPresent());
        assertThat(optionalWeighbridge.get().getWeighbridgeNumber()).isBetween(1, 3);
    }
}