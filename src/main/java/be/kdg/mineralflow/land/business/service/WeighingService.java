package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.service.externalApi.StockPortionDropAtWarehousePublisher;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseClient;
import be.kdg.mineralflow.land.business.util.WarehouseNumberResponse;
import be.kdg.mineralflow.land.business.util.WeighBridgeTicketResponse;
import be.kdg.mineralflow.land.business.util.WeighingResponse;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.exception.ProcessAlreadyFulfilledException;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class WeighingService {
    public static final Logger logger = Logger
            .getLogger(WeighingService.class.getName());
    private final UnloadingRequestRepository unloadingRequestRepository;
    private final StockPortionDropAtWarehousePublisher stockPortionDropAtWarehousePublisher;
    private final WarehouseClient warehouseClient;
    private final WeighBridgeTicketGeneratingService weighBridgeTicketGeneratingService;
    private final WarehouseNumberTicketGeneratingService warehouseNumberTicketGeneratingService;

    public WeighingService(UnloadingRequestRepository unloadingRequestRepository,
                           StockPortionDropAtWarehousePublisher truckArrivalAtWarehouseService,
                           WarehouseClient warehouseService, WeighBridgeTicketGeneratingService weighBridgeTicketGeneratingService, WarehouseNumberTicketGeneratingService warehouseNumberTicketGeneratingService) {
        this.unloadingRequestRepository = unloadingRequestRepository;
        this.stockPortionDropAtWarehousePublisher = truckArrivalAtWarehouseService;
        this.warehouseClient = warehouseService;
        this.weighBridgeTicketGeneratingService = weighBridgeTicketGeneratingService;
        this.warehouseNumberTicketGeneratingService = warehouseNumberTicketGeneratingService;
    }


    public WeighingResponse processWeighingOperation(String licensePlate, double weight, ZonedDateTime timestamp) {
        logger.info(String.format("Processing weighing operation for truck with license plate: %s", licensePlate));
        UnloadingRequest unloadingRequest = getUnloadingRequestByByLicensePlateAndNoVisitLeavingTime(licensePlate);

        if (unloadingRequest.hasBeenOnWeighBridge()) {
            WeighBridgeTicketResponse weighBridgeTicketResponse = processWeighingOperationAtDeparture(unloadingRequest, weight, timestamp, licensePlate);
            weighBridgeTicketGeneratingService.generateWeighBridgeTicketPdf(weighBridgeTicketResponse);
            logger.info(String.format("Weighing operation has successfully updated weighBridgeTicket for truck with license plate: %s", licensePlate));
            return weighBridgeTicketResponse;
        } else {
            WarehouseNumberResponse warehouseNumberResponse = processWeighingOperationAtArrival(unloadingRequest, licensePlate, weight, timestamp);
            warehouseNumberTicketGeneratingService.generateWeighBridgeTicketPdf(warehouseNumberResponse);
            logger.info(String.format("Weighing operation has successfully added weighBridgeTicket for truck with license plate: %s", licensePlate));
            return warehouseNumberResponse;
        }
    }

    private UnloadingRequest getUnloadingRequestByByLicensePlateAndNoVisitLeavingTime(String licensePlate) {
        Optional<UnloadingRequest> optionalUnloadingRequest = unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate);
        if (optionalUnloadingRequest.isEmpty()) {
            String messageException = String.format("An unloading request from license plate %s where it already arrived but has not left, was not found", licensePlate);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        return optionalUnloadingRequest.get();
    }

    private WeighBridgeTicketResponse processWeighingOperationAtDeparture(UnloadingRequest unloadingRequest, double endWeightAmountInTon,
                                                                          ZonedDateTime endWeightTimestamp, String licensePlate) {

        updateWeightBridgeTicketAtDeparture(unloadingRequest, endWeightAmountInTon, endWeightTimestamp);
        double netWeightInTon = unloadingRequest.getNetWeightOfWeighBridgeTicket();
        stockPortionDropAtWarehousePublisher.handleDepartureFromWarehouse(unloadingRequest.getVendorId(),
                unloadingRequest.getResourceId(), netWeightInTon, endWeightTimestamp);
        return unloadingRequest.getWeighBridgeTicketData(licensePlate);
    }

    private void updateWeightBridgeTicketAtDeparture(UnloadingRequest unloadingRequest, double endWeightAmountInTon,
                                                     ZonedDateTime endWeightTimestamp) {
        try {
            unloadingRequest.updateWeightBridgeTicketAtDeparture(endWeightAmountInTon, endWeightTimestamp);
        } catch (ProcessAlreadyFulfilledException e) {
            String text = String.format("Can't update weighbridge ticket, because it already has been updated: %s", e.getMessage());
            logger.severe(text);
            throw new ProcessAlreadyFulfilledException(text);
        }
        unloadingRequestRepository.save(unloadingRequest);
    }

    private WarehouseNumberResponse processWeighingOperationAtArrival(UnloadingRequest unloadingRequest, String licensePlate, double weight, ZonedDateTime timestamp) {
        addWeightBridgeTicketToVisit(unloadingRequest, weight, timestamp);

        int warehouseNumber = warehouseClient.getWarehouseNumber(unloadingRequest.getVendorId(), unloadingRequest.getResourceId());
        logger.info(String.format("Processing weighing operation has been successful,warehouse number %d for truck with license plate: %s", warehouseNumber, licensePlate));
        return new WarehouseNumberResponse(warehouseNumber);
    }


    private void addWeightBridgeTicketToVisit(UnloadingRequest unloadingRequest, double weight, ZonedDateTime timestamp) {
        unloadingRequest.addWeightBridgeTicketToVisit(weight, timestamp);
        unloadingRequestRepository.save(unloadingRequest);
    }

}
