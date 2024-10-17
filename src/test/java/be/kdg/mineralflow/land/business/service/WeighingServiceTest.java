package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.*;
import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import be.kdg.mineralflow.land.business.domain.warehouse.Vendor;
import be.kdg.mineralflow.land.business.service.externalApi.StockPortionDropAtWarehousePublisher;
import be.kdg.mineralflow.land.business.service.externalApi.WarehouseClient;
import be.kdg.mineralflow.land.business.util.WarehouseNumberResponse;
import be.kdg.mineralflow.land.business.util.WeighBridgeTicketResponse;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.exception.ProcessAlreadyFulfilledException;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;


class WeighingServiceTest extends TestContainer {

    @Autowired
    private WeighingService weighingService;
    @MockBean
    private UnloadingRequestRepository unloadingRequestRepository;
    @MockBean
    private StockPortionDropAtWarehousePublisher stockPortionDropAtWarehousePublisher;
    @MockBean
    private WarehouseClient warehouseClient;

    @Test
    void processWeighingOperation_When_UnloadingRequest_Exists_With_Visit_And_First_Time_Over_Bridge() {
        //ARRANGE
        int warehouseNumber = 5;
        String licensePlate = "HEARTXO";
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        double startWeight = 123;
        ZonedDateTime creationDate = ZonedDateTime.of(2010, 10, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        ZonedDateTime arrivalTimeBridge = ZonedDateTime.of(2010, 10, 1, 1, 20, 1, 1, ZoneOffset.UTC);
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
        Mockito.when(unloadingRequestRepository.save(unloadingRequest))
                .thenReturn(unloadingRequest);
        //ACT
        WarehouseNumberResponse weighingResponse = (WarehouseNumberResponse) weighingService.processWeighingOperation(licensePlate, startWeight, arrivalTimeBridge);

        //ASSERT
        Weighing startWeighingOfActualTicket = testVisit.getWeighbridgeTicket().getStartWeight();
        assertEquals(weighingResponse.warehouseNumber(), warehouseNumber);
        assertEquals(startWeighingOfActualTicket.getAmountInTon(), startWeight);
        assertEquals(startWeighingOfActualTicket.getTimestamp(), arrivalTimeBridge);
        Mockito.verify(warehouseClient, Mockito.times(1))
                .getWarehouseNumber(vendorId, resourceId);
    }

    @Test
    void processWeighingOperation_When_UnloadingRequest_Does_Not_Exists_With_Visit_And_No_Leaving_Time() {
        //ARRANGE
        int warehouseNumber = 5;
        String licensePlate = "HEARTXO";
        ZonedDateTime arrivalTimeBridge = ZonedDateTime.of(2010, 10, 1, 1, 20, 1, 1, ZoneOffset.UTC);
        Vendor vendorTest = new Vendor(UUID.randomUUID(), "johnson & Johnson");
        Resource resource = new Resource(UUID.randomUUID(), "Geton");


        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.empty());
        Mockito.when(warehouseClient.getWarehouseNumber(vendorTest.getId(), resource.getId()))
                .thenReturn(warehouseNumber);
        //ACT
        // ASSERT
        assertThrows(NoItemFoundException.class, () -> weighingService.processWeighingOperation(licensePlate, 123, arrivalTimeBridge));
    }

    @Test
    void processWeighingOperation_When_UnloadingRequest_Exists_With_Visit_And_Second_Time_Over_Bridge() {
        //ARRANGE
        String licensePlate = "HEARTXO";
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        double startWeight = 250;
        double endWeight = 180;
        double netWeight = startWeight - endWeight;
        ZonedDateTime creationDate = ZonedDateTime.of(2010, 10, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        ZonedDateTime arrivalTimeBridge = ZonedDateTime.of(2010, 10, 1, 1, 20, 1, 1, ZoneOffset.UTC);
        ZonedDateTime departureTimeBridge = ZonedDateTime.of(2010, 10, 1, 1, 30, 1, 1, ZoneOffset.UTC);
        Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
        Resource resource = new Resource(resourceId, "Geton");
        Visit testVisit = new Visit(creationDate);
        testVisit.setWeighbridgeTicket(startWeight, arrivalTimeBridge);
        WeighbridgeTicket ticket = testVisit.getWeighbridgeTicket();

        UnloadingRequest unloadingRequest = new UnloadingWithoutAppointment(licensePlate, creationDate);
        unloadingRequest.setVendor(vendorTest);
        unloadingRequest.setResource(resource);
        unloadingRequest.setVisit(testVisit);
        WeighBridgeTicketResponse weighBridgeTicketResponse =
                new WeighBridgeTicketResponse(startWeight, arrivalTimeBridge, endWeight, departureTimeBridge, licensePlate);

        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.of(unloadingRequest));
        doNothing().when(stockPortionDropAtWarehousePublisher)
                .handleDepartureFromWarehouse(vendorId, resourceId, startWeight, arrivalTimeBridge);
        Mockito.when(unloadingRequestRepository.save(unloadingRequest))
                .thenReturn(unloadingRequest);
        //ACT
        WeighBridgeTicketResponse actualWeighingResponse = (WeighBridgeTicketResponse) weighingService.processWeighingOperation(licensePlate, endWeight, departureTimeBridge);

        //ASSERT
        assertEquals(weighBridgeTicketResponse, actualWeighingResponse);
        assertEquals(ticket.getStartWeight().getAmountInTon(), startWeight);
        assertEquals(ticket.getStartWeight().getTimestamp(), arrivalTimeBridge);
        assertEquals(ticket.getEndWeight().getAmountInTon(), endWeight);
        assertEquals(ticket.getEndWeight().getTimestamp(), departureTimeBridge);
        assertEquals(ticket.getNetWeight(), netWeight);

        Mockito.verify(stockPortionDropAtWarehousePublisher, Mockito.times(1))
                .handleDepartureFromWarehouse(vendorId, resourceId, netWeight, departureTimeBridge);
    }

    @Test
    void processWeighingOperation_Should_Throw_Exception_When_WeighBridgeTicket_Has_Already_Been_Updated() {
        //ARRANGE
        String licensePlate = "HEARTXO";
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        double startWeight = 250;
        double endWeight = 180;
        double netWeight = startWeight - endWeight;
        ZonedDateTime creationDate = ZonedDateTime.of(2010, 10, 1, 1, 1, 1, 1, ZoneOffset.UTC);
        ZonedDateTime arrivalTimeBridge = ZonedDateTime.of(2010, 10, 1, 1, 20, 1, 1, ZoneOffset.UTC);
        ZonedDateTime departureTimeBridge = ZonedDateTime.of(2010, 10, 1, 1, 30, 1, 1, ZoneOffset.UTC);
        Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
        Resource resource = new Resource(resourceId, "Geton");
        Visit testVisit = new Visit(creationDate);
        testVisit.setWeighbridgeTicket(startWeight, arrivalTimeBridge);
        WeighbridgeTicket ticket = testVisit.getWeighbridgeTicket();
        ticket.updateEndWeight(endWeight, departureTimeBridge);

        UnloadingRequest unloadingRequest = new UnloadingWithoutAppointment(licensePlate, creationDate);
        unloadingRequest.setVendor(vendorTest);
        unloadingRequest.setResource(resource);
        unloadingRequest.setVisit(testVisit);

        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.of(unloadingRequest));
        doNothing().when(stockPortionDropAtWarehousePublisher)
                .handleDepartureFromWarehouse(vendorId, resourceId, startWeight, arrivalTimeBridge);
        Mockito.when(unloadingRequestRepository.save(unloadingRequest))
                .thenReturn(unloadingRequest);
        //ACT
        // ASSERT
        assertThrows(ProcessAlreadyFulfilledException.class, () -> weighingService.processWeighingOperation(licensePlate, endWeight, departureTimeBridge));

        Mockito.verify(stockPortionDropAtWarehousePublisher, Mockito.times(0))
                .handleDepartureFromWarehouse(vendorId, resourceId, netWeight, departureTimeBridge);
    }
}