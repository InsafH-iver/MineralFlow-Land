package be.kdg.mineralflow.land.business.service.externalApi;

import be.kdg.mineralflow.land.config.messaging.RabbitConfigProperties;
import be.kdg.mineralflow.land.presentation.controller.dto.StockPortionDropDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class StockPortionDropAtWarehousePublisher {
    public static final Logger logger = Logger
            .getLogger(StockPortionDropAtWarehousePublisher.class.getName());
    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfigProperties configProperties;

    public StockPortionDropAtWarehousePublisher(RabbitTemplate rabbitTemplate, RabbitConfigProperties configProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.configProperties = configProperties;
    }

    public void handleDepartureFromWarehouse(UUID vendorId, UUID resourceId,
                                            double weight ,ZonedDateTime endWeightTime ,UUID unloadingRequestId){
        var dto = new StockPortionDropDto(vendorId, resourceId, weight, endWeightTime,unloadingRequestId);
        rabbitTemplate.convertAndSend(configProperties.getExchangeName(),
                configProperties.getTruckDepartureFromWeighingBridgeRoutingKey(), dto);
        logger.info(String.format("Event dropping Stock portion (weight - %f ton) of at warehouse of vendor %s and resource %s has been published",weight ,vendorId, resourceId));
    }
}
