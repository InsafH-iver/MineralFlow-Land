package be.kdg.mineralflow.land.config.messaging;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitTopology {

    private final RabbitConfigProperties rabbitConfigProperties;

    public RabbitTopology(RabbitConfigProperties rabbitConfigProperties) {
        this.rabbitConfigProperties = rabbitConfigProperties;
    }

    @Bean
    TopicExchange warehouseTopicExchange() {
        return new TopicExchange(rabbitConfigProperties.getWarehouseExchangeName());
    }
}
