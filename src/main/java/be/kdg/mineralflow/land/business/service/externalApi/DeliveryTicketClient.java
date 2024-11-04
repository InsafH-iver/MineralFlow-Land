package be.kdg.mineralflow.land.business.service.externalApi;

import be.kdg.mineralflow.land.business.util.response.DeliveryDataResponse;
import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.exception.RestClientErrorException;
import be.kdg.mineralflow.land.presentation.controller.dto.DeliveryTicketDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Service
public class DeliveryTicketClient {
    public static final Logger logger = Logger
            .getLogger(DeliveryTicketClient.class.getName());
    private final RestClient restClient;

    public DeliveryTicketClient(RestClient.Builder restClientBuilder, ConfigProperties configProperties) {
        String baseUrl = String.format("%s%s",
                configProperties.getWarehouseHostAddress(), configProperties.getDeliveryTicketRestUrl());
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public DeliveryTicketDto addDeliveryTicket(DeliveryDataResponse deliveryDataResponse) {
        logger.info(String.format("Making rest call to warehouse to make a delivery ticket delivered at %s" +
                        " with vendor id %s and resource id %s"
                , deliveryDataResponse.deliveryTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                , deliveryDataResponse.vendorId(), deliveryDataResponse.resourceId()));

        DeliveryTicketDto deliveryTicketDto;
        try {
            deliveryTicketDto = this.restClient.post().body(deliveryDataResponse).retrieve().body(DeliveryTicketDto.class);
        } catch (RestClientException exception) {
            String messageException = String.format("Call to warehouse server for warehouse number has failed, %s", exception.getMessage());
            logger.severe(messageException);
            throw new RestClientErrorException(messageException, exception);
        }

        checkDeliveryTicketExistence(deliveryTicketDto, deliveryDataResponse);

        logger.info(String.format("Successfully made delivery ticket for delivery done at %s" +
                        " with vendor id %s and resource id %s"
                , deliveryDataResponse.deliveryTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                , deliveryDataResponse.vendorId(), deliveryDataResponse.resourceId()));
        return deliveryTicketDto;
    }

    private void checkDeliveryTicketExistence(DeliveryTicketDto deliveryTicketDto, DeliveryDataResponse deliveryDataResponse) {
        if (deliveryTicketDto == null) {
            String messageException = String.format("No delivery ticket was made for delivery done at %s" +
                            " with vendor id %s and resource id %s"
                    , deliveryDataResponse.deliveryTime().format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
                    , deliveryDataResponse.vendorId(), deliveryDataResponse.resourceId());
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }
    }
}
