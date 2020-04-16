package cz.kodytek.eshop.api.rest.resources;

import cz.kodytek.logic.services.interfaces.ICategoryService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

}
