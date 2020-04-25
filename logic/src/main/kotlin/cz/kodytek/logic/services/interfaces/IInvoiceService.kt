package cz.kodytek.logic.services.interfaces

import cz.kodytek.logic.models.invoice.Invoice
import cz.kodytek.logic.services.exceptions.InvalidDeliveryMethodException
import cz.kodytek.logic.services.exceptions.InvalidPaymentMethodException
import cz.kodytek.logic.services.exceptions.InvalidProductException

interface IInvoiceService {

    @Throws(InvalidProductException::class, InvalidDeliveryMethodException::class, InvalidPaymentMethodException::class)
    fun create(invoice: Invoice): Invoice

}