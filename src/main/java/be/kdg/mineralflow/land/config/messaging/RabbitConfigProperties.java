package be.kdg.mineralflow.land.config.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "messaging")
public class RabbitConfigProperties {
    private String warehouseExchangeName;
    // publishers routing keys
    private String publisherTruckDepartureFromWeighingBridgeRoutingKey;

    public String getWarehouseExchangeName() {
        return warehouseExchangeName;
    }

    public String getPublisherTruckDepartureFromWeighingBridgeRoutingKey() {
        return publisherTruckDepartureFromWeighingBridgeRoutingKey;
    }

    public void setWarehouseExchangeName(String warehouseExchangeName) {
        this.warehouseExchangeName = warehouseExchangeName;
    }

    public void setPublisherTruckDepartureFromWeighingBridgeRoutingKey(String publisherTruckDepartureFromWeighingBridgeRoutingKey) {
        this.publisherTruckDepartureFromWeighingBridgeRoutingKey = publisherTruckDepartureFromWeighingBridgeRoutingKey;
    }
}
