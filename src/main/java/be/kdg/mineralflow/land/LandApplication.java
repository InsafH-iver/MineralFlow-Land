package be.kdg.mineralflow.land;

import be.kdg.mineralflow.land.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class LandApplication {
    public static void main(String[] args) {
        SpringApplication.run(LandApplication.class, args);
    }

}
