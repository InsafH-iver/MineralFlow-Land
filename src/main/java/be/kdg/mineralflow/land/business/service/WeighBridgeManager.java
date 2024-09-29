package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.util.WeighBridgeResponse;
import be.kdg.mineralflow.land.config.ConfigLoader;
import org.springframework.stereotype.Service;

import java.util.Random;

import static be.kdg.mineralflow.land.config.ConfigProperties.WEIGHBRIDGE_AMOUNT;

@Service
public class WeighBridgeManager {

    public WeighBridgeResponse getWeighBridgeNumber() {
        Random rand = new Random();

        int weighBridgeNumber = rand.nextInt(ConfigLoader.getProperty(WEIGHBRIDGE_AMOUNT) + 1);
        return new WeighBridgeResponse(weighBridgeNumber);
    }
}
