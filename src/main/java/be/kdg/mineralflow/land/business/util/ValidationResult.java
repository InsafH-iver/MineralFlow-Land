package be.kdg.mineralflow.land.business.util;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
    private final List<String> errors;

    public ValidationResult() {
        this.errors = new ArrayList<>();
    }
    public void appendMessage(String message){
        errors.add(message);
    }

    public String getErrors() {
        return String.join("\n", errors);
    }
}
