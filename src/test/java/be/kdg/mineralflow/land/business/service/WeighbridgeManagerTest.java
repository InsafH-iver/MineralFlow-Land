package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.Weighbridge;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.persistence.WeighbridgeRepository;
import be.kdg.mineralflow.land.testcontainer.TestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class WeighbridgeManagerTest {

    @Autowired
    private WeighbridgeManager weighBridgeManager;
    @MockBean
    private WeighbridgeRepository weighbridgeRepository;

    private final PostgreSQLContainer<?> postgreSQLContainer = TestContainerConfig.postgreSQLContainer;

    @BeforeEach
    void setUp() {
        postgreSQLContainer.start();
    }

    @Test
    void getWeighBridgeNumber_When_There_Is_Weighbridges_In_Db() {
        //ARRANGE
        int bridgeNumber = 1;
        Weighbridge expectedWeighBridge = new Weighbridge(bridgeNumber);

        Mockito.when(weighbridgeRepository.findTopByOrderByWeighbridgeNumber())
                .thenReturn(Optional.of(expectedWeighBridge));
        //ACT
        Weighbridge bridgeResponse = weighBridgeManager.getWeighBridgeNumber();

        //ASSERT
        assertThat(bridgeResponse.getWeighbridgeNumber()).isEqualTo(bridgeNumber);
        Mockito.verify(weighbridgeRepository, Mockito.times(1)).findTopByOrderByWeighbridgeNumber();
    }

    @Test
    void getWeighBridgeNumber_When_There_Are_No_Weighbridges_In_Db() {
        //ARRANGE
        Mockito.when(weighbridgeRepository.findTopByOrderByWeighbridgeNumber())
                .thenReturn(Optional.empty());
        //ASSERT
        assertThrows(NoItemFoundException.class, weighBridgeManager::getWeighBridgeNumber);
    }
}