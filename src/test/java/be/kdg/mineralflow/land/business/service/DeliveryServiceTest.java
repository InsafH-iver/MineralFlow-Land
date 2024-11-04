package be.kdg.mineralflow.land.business.service;

import be.kdg.mineralflow.land.TestContainer;
import be.kdg.mineralflow.land.business.domain.UnloadingRequest;
import be.kdg.mineralflow.land.business.domain.warehouse.Resource;
import be.kdg.mineralflow.land.business.domain.warehouse.Vendor;
import be.kdg.mineralflow.land.business.service.externalApi.DeliveryTicketClient;
import be.kdg.mineralflow.land.business.util.response.DeliveryDataResponse;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.exception.RestClientErrorException;
import be.kdg.mineralflow.land.persistence.UnloadingRequestRepository;
import be.kdg.mineralflow.land.presentation.controller.dto.DeliveryTicketDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeliveryServiceTest extends TestContainer {

    @Autowired
    private DeliveryService deliveryService;
    @MockBean
    private UnloadingRequestRepository unloadingRequestRepository;
    @MockBean
    private DeliveryTicketClient deliveryTicketClient;

    @Test
    void processDelivery_Should_Succeed_When_Warehouse_Exists() {
        //ARRANGE
        String licensePlate = "TEST-PD";
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 3, 5, 5, 0, 0, ZoneOffset.UTC);
        UnloadingRequest unloadingRequest = new UnloadingRequest(licensePlate);
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        int warehouseNumber = 1;
        String resourceName = "Geton";
        Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
        Resource resource = new Resource(resourceId, resourceName);
        unloadingRequest.setVendor(vendorTest);
        unloadingRequest.setResource(resource);

        DeliveryDataResponse deliveryDataResponse = new DeliveryDataResponse(
                unloadingRequest.getVendorId(), unloadingRequest.getResourceId(),
                deliveryTime, unloadingRequest.getId());
        DeliveryTicketDto deliveryTicketDto = new DeliveryTicketDto
                (resourceName, deliveryTime, warehouseNumber);

        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.of(unloadingRequest));
        Mockito.when(unloadingRequestRepository.save(unloadingRequest)).thenReturn(unloadingRequest);
        Mockito.when(deliveryTicketClient.addDeliveryTicket(deliveryDataResponse))
                .thenReturn(deliveryTicketDto);

        //ACT
        DeliveryTicketDto actualDeliveryTicketDto = deliveryService
                .processDelivery(licensePlate, deliveryTime);
        //ASSERT
        assertThat(actualDeliveryTicketDto).isEqualTo(deliveryTicketDto);
    }

    @Test
    void processDelivery_Should_Throw_Exception_When_Warehouse_Does_Not_Exist() {
        //ARRANGE
        String licensePlate = "TEST-PD";
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 3, 5, 5, 0, 0, ZoneOffset.UTC);

        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.empty());

        //ACT
        //ASSERT
        assertThrows(NoItemFoundException.class,() -> deliveryService
                .processDelivery(licensePlate, deliveryTime));
    }

    @Test
    void processDelivery_Should_Throw_Exception_When_DeliveryTicketClient_Throws_RestClientErrorException() {
        //ARRANGE
        String licensePlate = "TEST-PD";
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 3, 5, 5, 0, 0, ZoneOffset.UTC);
        UnloadingRequest unloadingRequest = new UnloadingRequest(licensePlate);
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        String resourceName = "Geton";
        Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
        Resource resource = new Resource(resourceId, resourceName);
        unloadingRequest.setVendor(vendorTest);
        unloadingRequest.setResource(resource);

        DeliveryDataResponse deliveryDataResponse = new DeliveryDataResponse(
                unloadingRequest.getVendorId(), unloadingRequest.getResourceId(),
                deliveryTime, unloadingRequest.getId());

        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.of(unloadingRequest));
        Mockito.when(unloadingRequestRepository.save(unloadingRequest)).thenReturn(unloadingRequest);
        Mockito.when(deliveryTicketClient.addDeliveryTicket(deliveryDataResponse))
                .thenThrow(RestClientErrorException.class);

        //ACT
        //ASSERT
        assertThrows(RestClientErrorException.class,() -> deliveryService
                .processDelivery(licensePlate, deliveryTime));
    }

    @Test
    void processDelivery_Should_Throw_Exception_When_DeliveryTicketClient_Throws_NoItemFoundException() {
        //ARRANGE
        String licensePlate = "TEST-PD";
        ZonedDateTime deliveryTime = ZonedDateTime.of(2024, 2, 3, 5, 5, 0, 0, ZoneOffset.UTC);
        UnloadingRequest unloadingRequest = new UnloadingRequest(licensePlate);
        UUID vendorId = UUID.randomUUID();
        UUID resourceId = UUID.randomUUID();
        String resourceName = "Geton";
        Vendor vendorTest = new Vendor(vendorId, "johnson & Johnson");
        Resource resource = new Resource(resourceId, resourceName);
        unloadingRequest.setVendor(vendorTest);
        unloadingRequest.setResource(resource);

        DeliveryDataResponse deliveryDataResponse = new DeliveryDataResponse(
                unloadingRequest.getVendorId(), unloadingRequest.getResourceId(),
                deliveryTime, unloadingRequest.getId());

        Mockito.when(unloadingRequestRepository.findFirstByLicensePlateAndVisit_LeavingTimeIsNull(licensePlate))
                .thenReturn(Optional.of(unloadingRequest));
        Mockito.when(unloadingRequestRepository.save(unloadingRequest)).thenReturn(unloadingRequest);
        Mockito.when(deliveryTicketClient.addDeliveryTicket(deliveryDataResponse))
                .thenThrow(NoItemFoundException.class);

        //ACT
        //ASSERT
        assertThrows(NoItemFoundException.class,() -> deliveryService
                .processDelivery(licensePlate, deliveryTime));
    }
}