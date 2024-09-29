package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.service.WeighBridgeManager;
import be.kdg.mineralflow.land.business.util.WeighBridgeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/WeighBridges")
public class WeighBridgeRestController {
    private final WeighBridgeManager weighBridgeManager;

    public WeighBridgeRestController(WeighBridgeManager weighBridgeManager) {
        this.weighBridgeManager = weighBridgeManager;
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public WeighBridgeResponse GetWeighBridgeNumber() {
        return weighBridgeManager.getWeighBridgeNumber();
    }
}
