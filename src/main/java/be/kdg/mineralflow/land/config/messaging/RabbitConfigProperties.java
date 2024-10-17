package be.kdg.mineralflow.land.config.messaging;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "messaging")
public class RabbitConfigProperties {
    private String exchangeName;
    private String truckDepartureFromWeighingBridgeRoutingKey;

    public String getTruckDepartureFromWeighingBridgeRoutingKey() {
        return truckDepartureFromWeighingBridgeRoutingKey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setTruckDepartureFromWeighingBridgeRoutingKey(String truckDepartureFromWeighingBridgeRoutingKey) {
        this.truckDepartureFromWeighingBridgeRoutingKey = truckDepartureFromWeighingBridgeRoutingKey;
    }
}
