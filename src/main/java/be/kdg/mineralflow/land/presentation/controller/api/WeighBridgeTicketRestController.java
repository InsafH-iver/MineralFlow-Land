package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.service.WeighingService;
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
    public int updateWeighBridgeTicket(@RequestBody WeighBridgeTicketDto weighBridgeTicketDto) {
        logger.info("The truck Call to update/ make weighbridge Ticket has been made");
        int warehouseNumber = weighingService.processWeighingOperation(weighBridgeTicketDto.licensePlate(),
                weighBridgeTicketDto.weight(),
                weighBridgeTicketDto.timestamp());
        logger.info(
                String.format("The truck with licenseplate %s has succesfully made a weightbridge ticket and can now go to warehouse %d",weighBridgeTicketDto.licensePlate(), warehouseNumber));
        return warehouseNumber;
    }
}
