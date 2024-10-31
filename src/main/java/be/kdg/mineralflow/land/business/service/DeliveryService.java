package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.service.externalApi.DeliveryTicketClient;
import be.kdg.mineralflow.land.business.util.DeliveryDataResponse;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import be.kdg.mineralflow.land.presentation.controller.dto.DeliveryTicketDto;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class DeliveryService {
    public static final Logger logger = Logger
            .getLogger(DeliveryService.class.getName());

    private final UnloadingRequestRepository unloadingRequestRepository;
    private final DeliveryTicketClient deliveryTicketClient;

    public DeliveryService(UnloadingRequestRepository unloadingRequestRepository, DeliveryTicketClient deliveryTicketClient) {
        this.unloadingRequestRepository = unloadingRequestRepository;
        this.deliveryTicketClient = deliveryTicketClient;
    }

    public DeliveryTicketDto processDelivery(String licensePlate, ZonedDateTime deliveryTime) {
        logger.info(String.format("Processing delivery at %s for truck with license plate: %s"
                , deliveryTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME), licensePlate));
        UnloadingRequest unloadingRequest = getUnloadingRequestByLicensePlateAndNoVisitLeavingTime(licensePlate);
        DeliveryTicketDto deliveryTicket = createDeliveryTicket(unloadingRequest, deliveryTime);
        logger.info(String.format("Delivery Ticket %s has successfully been created", deliveryTicket));
        return deliveryTicket;
    }

    private UnloadingRequest getUnloadingRequestByLicensePlateAndNoVisitLeavingTime(String licensePlate) {
        Optional<UnloadingRequest> optionalUnloadingRequest = unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate);
        if (optionalUnloadingRequest.isEmpty()) {
            String messageException = String.format("An unloading request with license plate %s where it already arrived but has not left, was not found", licensePlate);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        return optionalUnloadingRequest.get();
    }

    private DeliveryTicketDto createDeliveryTicket(UnloadingRequest unloadingRequest, ZonedDateTime deliveryTime) {
        DeliveryDataResponse deliveryDataResponse = new DeliveryDataResponse(
                unloadingRequest.getVendorId(), unloadingRequest.getResourceId(),
                deliveryTime, unloadingRequest.getId());
        return deliveryTicketClient.addDeliveryTicket(deliveryDataResponse);
    }
}
