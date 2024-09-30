package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.Weighbridge;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.persistence.WeighbridgeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class WeighbridgeManagerTest {

    @Autowired
    private WeighbridgeManager weighBridgeManager;
    @MockBean
    private WeighbridgeRepository weighbridgeRepository;

    static PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void getWeighBridgeNumber_When_There_Is_Weighbridges_In_Db() {
        //ARRANGE
        int bridgeNumber = 1;
        Weighbridge expectedWeighBridge = new Weighbridge(bridgeNumber);

        Mockito.when(weighbridgeRepository.findRandomWeighbridge())
                .thenReturn(Optional.of(expectedWeighBridge));
        //ACT
        Weighbridge bridgeResponse = weighBridgeManager.getWeighBridgeNumber();

        //ASSERT
        assertThat(bridgeResponse.getWeighbridgeNumber()).isEqualTo(bridgeNumber);
        Mockito.verify(weighbridgeRepository, Mockito.times(1)).findRandomWeighbridge();
    }

    @Test
    void getWeighBridgeNumber_When_There_Are_No_Weighbridges_In_Db() {
        //ARRANGE
        Mockito.when(weighbridgeRepository.findRandomWeighbridge())
                .thenReturn(Optional.empty());
        //ASSERT
        assertThrows(NoItemFoundException.class, weighBridgeManager::getWeighBridgeNumber);
    }
}