package cz.kodytek.eshop.api.rest.resources;

import cz.kodytek.logic.models.ProductRating;
import cz.kodytek.logic.services.interfaces.IProductService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    private IProductService productService;

    @GET
    @Path("{id}")
    public Response getDetail(@PathParam("id") long id) {
        try {
            return Response.ok(productService.getDetail(id)).build();
        } catch(NoResultException e) {
            return Response.status(404).build();
        }
    }

    @POST
    @Path("{id}/rating")
    public Response addRating(@PathParam("id") long id, @Valid ProductRating rating) {
        return Response.ok(productService.rate(id, rating)).build();
    }

}
