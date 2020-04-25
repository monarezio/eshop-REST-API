package cz.kodytek.eshop.api.rest.resources;

import cz.kodytek.logic.services.interfaces.IPaymentMethodService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("payments")
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource {

    @Inject
    private IPaymentMethodService paymentMethodService;

    @GET
    public Response getAll() {
        return Response.ok(paymentMethodService.getAll()).build();
    }

}
