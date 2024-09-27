package be.kdg.mineralflow.land.config;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

@Component()
public class ConfigLoader {
    public static final Logger logger = Logger
            .getLogger(ConfigLoader.class.getName());
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                PROPERTIES.load(input);
                logger.info("Config properties loaded successfully");
            } else {
                logger.severe("Properties file not found");
                throw new IOException("Properties file not found");
            }
        } catch (IOException e) {
            logger.severe(String.format("Error loading properties file: %s", e));
        }
    }

    public static int getProperty(String key) {
        String property = PROPERTIES.getProperty(key);
        logger.info(String.format("Fetching property with value %s", property));
        return Integer.parseInt(property);
    }
}