package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.service.externalApi.TruckArrivalAtWarehousePublisher;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseClient;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
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
    private final TruckArrivalAtWarehousePublisher truckArrivalAtWarehousePublisher;
    private final WarehouseClient warehouseClient;

    public WeighingService(UnloadingRequestRepository unloadingRequestRepository, TruckArrivalAtWarehousePublisher truckArrivalAtWarehouseService, WarehouseClient warehouseService) {
        this.unloadingRequestRepository = unloadingRequestRepository;
        this.truckArrivalAtWarehousePublisher = truckArrivalAtWarehouseService;
        this.warehouseClient = warehouseService;
    }


    public int processWeighingOperation(String licensePlate, double weight, ZonedDateTime timestamp) {
        logger.info(String.format("Processing weighing operation for truck with license plate: %s", licensePlate));
        Optional<UnloadingRequest> optionalUnloadingRequest = unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate);
        if (optionalUnloadingRequest.isEmpty()) {
            String messageException = String.format("An unloading request from license plate %s where it already arrived but has not left, was not found", licensePlate);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
        UnloadingRequest unloadingRequest = optionalUnloadingRequest.get();

        addWeightBridgeTicketToVisit(unloadingRequest, weight, timestamp);

        int warehouseNumber = processTruckArrivalAtWarehouse(unloadingRequest, timestamp);
        logger.info(String.format("Processing weighing operation has been successful,warehouse number %d for truck with license plate: %s", warehouseNumber, licensePlate));
        return warehouseNumber;
    }

    private void addWeightBridgeTicketToVisit(UnloadingRequest unloadingRequest, double weight, ZonedDateTime timestamp) {
        unloadingRequest.addWeightBridgeTicketToVisit(weight, timestamp);
        unloadingRequestRepository.save(unloadingRequest);
    }

    private int processTruckArrivalAtWarehouse(UnloadingRequest unloadingRequest, ZonedDateTime timestamp) {
        truckArrivalAtWarehousePublisher.handleTruckArrivalAtWarehouse(
                unloadingRequest.getVendorId(), unloadingRequest.getResourceId(), timestamp);

        return warehouseClient.getWarehouseNumber(unloadingRequest.getVendorId(), unloadingRequest.getResourceId());
    }
}
