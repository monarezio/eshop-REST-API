package cz.kodytek.logic.mappers

import cz.kodytek.logic.mappers.interfaces.IMapper
import cz.kodytek.logic.models.invoice.Invoice
import cz.kodytek.logic.models.invoice.InvoiceProduct
import java.time.LocalDate
import cz.kodytek.eshop.data.entities.Invoice as DbInvoice

object InvoiceMapper : IMapper<DbInvoice, Invoice> {
    override fun mapTo(value: DbInvoice): Invoice {
        return Invoice(
                value.variableSymbol,
                PersonMapper.mapTo(value.person),
                CompanyMapper.mapTo(value.company),
                value.paymentMethod!!.id,
                value.deliveryMethod!!.id,
                value.products.map { InvoiceProduct(it.unitCount, ProductMapper.mapTo(it.product!!)) },
                value.note
        )
    }

    override fun mapFrom(value: Invoice): DbInvoice {
        return DbInvoice(
                null,
                "",
                PersonMapper.mapFrom(value.person!!),
                CompanyMapper.mapFrom(value.company),
                null, null,
                setOf(), value.note, LocalDate.now()
        )
    }
}