package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.UnloadingWithoutAppointment;
import be.kdg.mineralflow.land.business.domain.Visit;
import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import be.kdg.mineralflow.land.business.domain.warehouse.Vendor;
import be.kdg.mineralflow.land.business.service.externalApi.TruckArrivalAtWarehousePublisher;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseClient;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class WeighingServiceTest extends TestContainer {

    @Autowired
    private WeighingService weighingService;
    @MockBean
    private UnloadingRequestRepository unloadingRequestRepository;
    @MockBean
    private TruckArrivalAtWarehousePublisher truckArrivalAtWarehousePublisher;
    @MockBean
    private WarehouseClient warehouseClient;

    @Test
    void processWeighingOperation_When_UnloadingRequest_Exists_With_Visit_And_No_Leaving_Time() {
        //ARRANGE
        int warehouseNumber = 5;
        String licensePlate = "HEARTXO";
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        ZonedDateTime creationDate = ZonedDateTime.of(2010, 10, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        ZonedDateTime arrivalBridge = ZonedDateTime.of(2010, 10, 1, 1, 20, 1, 1, ZoneOffset.UTC);
        Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
        Resource resource = new Resource(resourceId, "Geton");
        Visit testVisit = new Visit(creationDate);

        UnloadingRequest unloadingRequest = new UnloadingWithoutAppointment(licensePlate, creationDate);
        unloadingRequest.setVendor(vendorTest);
        unloadingRequest.setResource(resource);
        unloadingRequest.setVisit(testVisit);

        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.of(unloadingRequest));
        Mockito.when(warehouseClient.getWarehouseNumber(vendorId, resourceId))
                .thenReturn(warehouseNumber);
        Mockito.doNothing().when(truckArrivalAtWarehousePublisher)
                .handleTruckArrivalAtWarehouse(vendorId, resourceId, arrivalBridge);
        Mockito.when(unloadingRequestRepository.save(unloadingRequest))
                .thenReturn(unloadingRequest);
        //ACT
        int warehouseNumberReturned = weighingService.processWeighingOperation(licensePlate, 123, arrivalBridge);

        //ASSERT
        assertEquals(warehouseNumberReturned, warehouseNumber);
        Mockito.verify(warehouseClient, Mockito.times(1))
                .getWarehouseNumber(vendorId, resourceId);
        Mockito.verify(truckArrivalAtWarehousePublisher, Mockito.times(1))
                .handleTruckArrivalAtWarehouse(vendorId, resourceId, arrivalBridge);
    }

    @Test
    void processWeighingOperation_When_UnloadingRequest_Does_Not_Exists_With_Visit_And_No_Leaving_Time() {
        //ARRANGE
        int warehouseNumber = 5;
        String licensePlate = "HEARTXO";
        ZonedDateTime arrivalBridge = ZonedDateTime.of(2010, 10, 1, 1, 20, 1, 1, ZoneOffset.UTC);
        Vendor vendorTest = new Vendor(UUID.randomUUID(), "johnson & Johnson");
        Resource resource = new Resource(UUID.randomUUID(), "Geton");


        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.empty());
        Mockito.when(warehouseClient.getWarehouseNumber(vendorTest.getId(), resource.getId()))
                .thenReturn(warehouseNumber);
        Mockito.doNothing().when(truckArrivalAtWarehousePublisher)
                .handleTruckArrivalAtWarehouse(vendorTest.getId(), resource.getId(), arrivalBridge);
        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () -> weighingService.processWeighingOperation(licensePlate, 123, arrivalBridge));
    }
}