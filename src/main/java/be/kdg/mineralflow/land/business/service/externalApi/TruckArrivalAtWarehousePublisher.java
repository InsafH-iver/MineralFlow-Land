package be.kdg.mineralflow.land.business.service.externalApi;

import be.kdg.mineralflow.land.config.messaging.RabbitConfigProperties;
import be.kdg.mineralflow.land.presentation.controller.dto.TruckArrivalAtWarehouseDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class TruckArrivalAtWarehousePublisher {
    public static final Logger logger = Logger
            .getLogger(TruckArrivalAtWarehousePublisher.class.getName());
    private final RabbitTemplate rabbitTemplate;
    private final RabbitConfigProperties configProperties;

    public TruckArrivalAtWarehousePublisher(RabbitTemplate rabbitTemplate, RabbitConfigProperties configProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.configProperties = configProperties;
    }

    public void handleTruckArrivalAtWarehouse(UUID vendorId, UUID resourceId,
                                              ZonedDateTime timestamp) {
        var dto = new TruckArrivalAtWarehouseDto(vendorId, resourceId, timestamp);
        rabbitTemplate.convertAndSend(configProperties.getExchangeName(),
                configProperties.getTruckArrivalAtWarehouseRoutingKey(), dto);
        logger.info(String.format("Event truck arrival at warehouse of vendor %s and resource %s has been published", vendorId, resourceId));
    }
}
