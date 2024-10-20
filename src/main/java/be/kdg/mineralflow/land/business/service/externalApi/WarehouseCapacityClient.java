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
public class WarehouseCapacityClient {
    public static final Logger logger = Logger
            .getLogger(WarehouseCapacityClient.class.getName());
    private final RestClient restClient;
    private final ConfigProperties configProperties;

    public WarehouseCapacityClient(RestClient.Builder restClientBuilder, ConfigProperties configProperties) {
        this.configProperties = configProperties;
        this.restClient = restClientBuilder.baseUrl(configProperties.getWarehouseCapacityBaseUrl()).build();
    }
    public boolean isWarehouseCapacityReached(UUID vendorId, UUID resourceId){
        Boolean capacityReached;
        try {
            capacityReached = this.restClient.get().uri(configProperties.getWarehouseCapacityIsFullUrl(), vendorId, resourceId).retrieve().body(Boolean.class);
        } catch (RestClientException exception) {
            String messageException = String.format("Call to warehouse server for warehouse capacity check has failed, %s", exception.getMessage());
            logger.severe(messageException);
            throw new RestClientErrorException(messageException, exception);
        }

        if (capacityReached == null) {
            String messageException = String.format("Warehouse capacity check returned null for %s and %s", vendorId, resourceId);
            logger.severe(messageException);
            throw new NoItemFoundException(messageException);
        }

        logger.info(String.format("Successfully gotten warehouse number for vendor id %s and resource id %s", vendorId, resourceId));
        return capacityReached;
    }
}

