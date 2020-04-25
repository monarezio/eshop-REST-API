package cz.kodytek.eshop.api.rest.resources;

import cz.kodytek.logic.services.interfaces.IDeliveryMethodService;
import cz.kodytek.logic.services.interfaces.IPaymentMethodService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("deliveries")
@Produces(MediaType.APPLICATION_JSON)
public class DeliveryResource {

    @Inject
    private IDeliveryMethodService deliveryMethodService;

    @GET
    public Response getAll() {
        return Response.ok(deliveryMethodService.getAll()).build();
    }

}
