package be.kdg.mineralflow.land.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class RestClientErrorException extends RuntimeException {

    public RestClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
