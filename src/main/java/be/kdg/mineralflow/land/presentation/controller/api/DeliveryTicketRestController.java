package be.kdg.mineralflow.land.presentation.controller.api;

import be.kdg.mineralflow.land.business.service.DeliveryService;
import be.kdg.mineralflow.land.presentation.controller.dto.DeliveryTicketDto;
import be.kdg.mineralflow.land.presentation.controller.dto.DeliveryTicketInputDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/deliveryTickets")
public class DeliveryTicketRestController {
    public static final Logger logger = Logger
            .getLogger(DeliveryTicketRestController.class.getName());
    private final DeliveryService deliveryService;

    public DeliveryTicketRestController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }


    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public DeliveryTicketDto createDeliveryTicket(@RequestBody DeliveryTicketInputDto deliveryTicketInputDto) {
        logger.info("The call to create delivery ticket has been made");
        DeliveryTicketDto deliveryTicketDto = deliveryService.processDelivery(
                deliveryTicketInputDto.licensePlate(), deliveryTicketInputDto.timestamp());
        logger.info(
                String.format("The truck with license plate %s has successfully made a delivery ticket"
                        , deliveryTicketInputDto.licensePlate()));
        return deliveryTicketDto;
    }
}
