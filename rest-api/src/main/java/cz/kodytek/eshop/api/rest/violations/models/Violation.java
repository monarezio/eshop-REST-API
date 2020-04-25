package cz.kodytek.eshop.api.rest.violations.models;

import java.io.Serializable;
import java.util.List;

public class Violation implements Serializable {

    private final String fieldName;
    private final String message;
    private final String value;

    public Violation(String fieldName, String violations, String value) {
        this.fieldName = fieldName;
        this.message = violations;
        this.value = value;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getMessage() {
        return message;
    }

    public String getValue() {
        return value;
    }
}
