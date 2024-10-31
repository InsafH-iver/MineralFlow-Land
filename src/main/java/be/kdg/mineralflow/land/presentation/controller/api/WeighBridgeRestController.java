package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.service.WeighbridgeService;
import be.kdg.mineralflow.land.presentation.controller.dto.WeighbridgeDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/WeighBridges")
public class WeighBridgeRestController {
    public static final Logger logger = Logger
            .getLogger(WeighBridgeRestController.class.getName());
    private final WeighbridgeService weighBridgeService;

    public WeighBridgeRestController(WeighbridgeService weighBridgeService) {
        this.weighBridgeService = weighBridgeService;
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public WeighbridgeDto GetWeighBridgeNumber() {
        logger.info("call to controller for weighbridge number has been made");
        return new WeighbridgeDto(weighBridgeService.getWeighBridgeNumber());
    }

}
