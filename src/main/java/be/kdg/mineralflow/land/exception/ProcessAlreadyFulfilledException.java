package be.kdg.mineralflow.land.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProcessAlreadyFulfilledException extends RuntimeException {
    public ProcessAlreadyFulfilledException(String message) {
        super(message);
    }
}
