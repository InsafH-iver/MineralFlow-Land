package be.kdg.mineralflow.land.business.service.externalApi;

import be.kdg.mineralflow.land.config.ConfigProperties;
import be.kdg.mineralflow.land.exception.NoItemFoundException;
import be.kdg.mineralflow.land.exception.RestClientErrorException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class WarehouseClient {
    public static final Logger logger = Logger
            .getLogger(WarehouseClient.class.getName());
    private final RestClient restClient;
    private final ConfigProperties configProperties;

    public WarehouseClient(RestClient.Builder restClientBuilder, ConfigProperties configProperties) {
        this.configProperties = configProperties;
        String baseUrl = String.format("%s%s",
                configProperties.getWarehouseHostAddress(), configProperties.getWarehouseRestUrl());
        this.restClient = restClientBuilder.baseUrl(baseUrl).build();
    }

    public int getWarehouseNumber(UUID vendorId, UUID resourceId) {
        logger.info(String.format("Making rest call to warehouse for warehouse number of vendor id %s and resource id %s", vendorId, resourceId));

        Integer warehouseNumber;
        try {
            warehouseNumber = this.restClient.get().uri(configProperties.getWarehouseNumberUrl(), vendorId, resourceId).retrieve().body(Integer.class);
        } catch (RestClientException exception) {
            String messageException = String.format("Call to warehouse server for warehouse number has failed, %s", exception.getMessage());
            logger.severe(messageException);
            throw new RestClientErrorException(messageException, exception);
        }

        if (warehouseNumber == null) {
            String messageException = String.format("No warehouse number was found for %s and %s", vendorId, resourceId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }

        logger.info(String.format("Successfully gotten warehouse number for vendor id %s and resource id %s", vendorId, resourceId));
        return warehouseNumber;
    }
}
