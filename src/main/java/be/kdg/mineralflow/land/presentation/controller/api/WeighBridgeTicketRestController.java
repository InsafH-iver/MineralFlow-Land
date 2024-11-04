package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.service.WeighingService;
import be.kdg.mineralflow.land.business.util.response.WeighingResponse;
import be.kdg.mineralflow.land.presentation.controller.dto.WeighBridgeTicketDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/WeighBridgeTickets")
public class WeighBridgeTicketRestController {
    public static final Logger logger = Logger
            .getLogger(WeighBridgeTicketRestController.class.getName());
    private final WeighingService weighingService;

    public WeighBridgeTicketRestController(WeighingService weighingService) {
        this.weighingService = weighingService;
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public WeighingResponse updateWeighBridgeTicket(@RequestBody WeighBridgeTicketDto weighBridgeTicketDto) {
        logger.info("The truck Call to update/ make weighbridge Ticket has been made");
        WeighingResponse weighingResponse = weighingService.processWeighingOperation(weighBridgeTicketDto.licensePlate(),
                weighBridgeTicketDto.weight(),
                weighBridgeTicketDto.timestamp());
        logger.info(
                String.format("The truck with license plate %s has successfully made/ updated a weightbridge ticket",weighBridgeTicketDto.licensePlate()));
        return weighingResponse;
    }
}
