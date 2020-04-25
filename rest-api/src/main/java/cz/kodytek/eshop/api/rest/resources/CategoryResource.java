package cz.kodytek.eshop.api.rest.resources;

import cz.kodytek.logic.models.Order;
import cz.kodytek.logic.models.ProductOrderBy;
import cz.kodytek.logic.services.interfaces.ICategoryService;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResource {

    @Inject
    private ICategoryService categoryService;

    @GET
    public Response index() {
        return Response.ok(categoryService.getAll()).build();
    }

    @GET
    @Path("{id}")
    public Response detail(
            @PathParam("id") int id,
            @QueryParam("page") int page,
            @QueryParam("search") @DefaultValue("") String search,
            @QueryParam("orderBy") @DefaultValue("id") ProductOrderBy orderBy,
            @QueryParam("order") @DefaultValue("asc") Order order
            ) {
        try {
            return Response.ok(categoryService.getDetail(id, page, search, orderBy, order)).build();
        } catch (NoResultException e) {
            return Response.status(404).build();
        }
    }

}
