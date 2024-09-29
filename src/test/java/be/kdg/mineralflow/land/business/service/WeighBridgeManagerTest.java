package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.util.WeighBridgeResponse;
import be.kdg.mineralflow.land.config.ConfigLoader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static be.kdg.mineralflow.land.config.ConfigProperties.WEIGHBRIDGE_AMOUNT;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WeighBridgeManagerTest {

    @Autowired
    private WeighBridgeManager weighBridgeManager;

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