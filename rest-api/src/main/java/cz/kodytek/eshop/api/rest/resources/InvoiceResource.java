package cz.kodytek.eshop.api.rest.resources;

import cz.kodytek.logic.models.invoice.Invoice;
import cz.kodytek.logic.services.exceptions.InvalidDeliveryMethodException;
import cz.kodytek.logic.services.exceptions.InvalidPaymentMethodException;
import cz.kodytek.logic.services.exceptions.InvalidProductException;
import cz.kodytek.logic.services.interfaces.IInvoiceService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("invoices")
@Produces(MediaType.APPLICATION_JSON)
public class InvoiceResource {

    @Inject
    private IInvoiceService invoiceService;

    @POST
    public Response create(@Valid Invoice invoice) {
        try {
            return Response.ok(invoiceService.create(invoice)).build();
        } catch (InvalidProductException e) {
            Map<String, List<Long>> errorMessage = new HashMap<>();
            errorMessage.put("invalidProducts", e.getProducts());
            return Response.status(400).entity(errorMessage).build();
        } catch (InvalidDeliveryMethodException e) {
            Map<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Invalid delivery method id");
            return Response.status(400).entity(errorMessage).build();
        } catch (InvalidPaymentMethodException e) {
            Map<String, String> errorMessage = new HashMap<>();
            errorMessage.put("error", "Invalid payment method id");
            return Response.status(400).entity(errorMessage).build();
        }
    }

}
