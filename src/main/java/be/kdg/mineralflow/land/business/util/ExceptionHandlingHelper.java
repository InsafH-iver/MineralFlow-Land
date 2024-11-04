package be.kdg.mineralflow.land.business.util;



import be.kdg.mineralflow.land.exception.NoItemFoundException;

import java.util.logging.Logger;

public class ExceptionHandlingHelper {
    public static final Logger logger = Logger
            .getLogger(ExceptionHandlingHelper.class.getName());

    public static NoItemFoundException logAndThrowNotFound(String message, Object... args) {
        String formattedMessage = String.format(message, args);
        logger.severe(formattedMessage);
        return new NoItemFoundException(formattedMessage);
    }
}