package cz.kodytek.eshop.api.rest.violations.mappers;

import cz.kodytek.eshop.api.rest.violations.models.Violation;

import javax.validation.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.*;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        Response.ResponseBuilder r = Response.status(400);

        List<Violation> violations = new ArrayList<>();
        e.getConstraintViolations().forEach(f -> {
            String[] fieldNameParts = f.getPropertyPath().toString().split("\\.");
            violations.add(new Violation(fieldNameParts[fieldNameParts.length - 1], f.getMessage(), f.getInvalidValue() == null ? null : f.getInvalidValue().toString()));
        });

        return r.entity(violations).build();
    }
}