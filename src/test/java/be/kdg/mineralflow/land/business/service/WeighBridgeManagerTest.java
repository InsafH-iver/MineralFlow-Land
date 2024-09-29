package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.util.WeighBridgeResponse;
import be.kdg.mineralflow.land.config.ConfigLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static be.kdg.mineralflow.land.config.ConfigProperties.WEIGHBRIDGE_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WeighBridgeManagerTest {

    @Autowired
    private WeighBridgeManager weighBridgeManager;

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
    void getWeighBridgeNumber() {
        //ARRANGE
        int amountOfWeighBridge = ConfigLoader.getProperty(WEIGHBRIDGE_AMOUNT);

        //ACT
        WeighBridgeResponse bridgeResponse = weighBridgeManager.getWeighBridgeNumber();

        //ASSERT
        assertThat(bridgeResponse.weighbridgeNumber()).isBetween(0, amountOfWeighBridge);
    }
}