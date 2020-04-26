package cz.kodytek.eshop.api.rest.filters;

import cz.kodytek.logic.services.interfaces.IApiAccessKeyService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Provider
public class AccessKeyFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    private HttpServletRequest servletRequest;

    @Inject
    private IApiAccessKeyService apiAccessKeyService;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String token = containerRequestContext.getHeaderString("access-token");
        Map<String, String> errors = new HashMap<>();

        if (token == null) {
            errors.put("error", "Invalid access key. Add a header `access-token` with your access key to the request.");
            containerRequestContext.abortWith(Response.status(401).entity(errors).build());
        } else
            try {
                apiAccessKeyService.log(token, containerRequestContext.getUriInfo().getPath(), servletRequest.getRemoteAddr(), ""); // TODO: Save somehow body
            } catch (NoResultException e) {
                errors.put("error", "Invalid access key.");
                containerRequestContext.abortWith(Response.status(401).entity(errors).build());
            }
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext) throws IOException {
    }
}
