package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.service.WeighbridgeManager;
import be.kdg.mineralflow.land.presentation.controller.dto.WeighbridgeDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/WeighBridges")
public class WeighBridgeRestController {
    private final WeighbridgeManager weighBridgeManager;

    public WeighBridgeRestController(WeighbridgeManager weighBridgeManager) {
        this.weighBridgeManager = weighBridgeManager;
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public WeighbridgeDto GetWeighBridgeNumber() {
        return new WeighbridgeDto(weighBridgeManager.getWeighBridgeNumber());
    }
}
