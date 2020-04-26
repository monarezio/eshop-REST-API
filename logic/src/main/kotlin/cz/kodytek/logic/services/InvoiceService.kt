package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.eshop.data.entities.Category
import cz.kodytek.eshop.data.entities.Product
import cz.kodytek.logic.mappers.DeliveryMapper
import cz.kodytek.logic.mappers.InvoiceMapper
import cz.kodytek.logic.mappers.PaymentMapper
import cz.kodytek.logic.mappers.ProductMapper
import cz.kodytek.logic.models.invoice.Invoice
import cz.kodytek.logic.services.exceptions.InvalidDeliveryMethodException
import cz.kodytek.logic.services.exceptions.InvalidPaymentMethodException
import cz.kodytek.logic.services.exceptions.InvalidProductException
import cz.kodytek.logic.services.interfaces.IDeliveryMethodService
import cz.kodytek.logic.services.interfaces.IInvoiceService
import cz.kodytek.logic.services.interfaces.IPaymentMethodService
import cz.kodytek.logic.services.interfaces.IProductService
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Path
import cz.kodytek.eshop.data.entities.InvoiceProduct as DbInvoiceProduct
import cz.kodytek.eshop.data.entities.Invoice as DbInvoice

@ApplicationScoped
open class InvoiceService : IInvoiceService {

    @Inject
    private lateinit var paymentMethodService: IPaymentMethodService

    @Inject
    private lateinit var deliveryMethodService: IDeliveryMethodService

    @Inject
    private lateinit var productService: IProductService

    override fun create(invoice: Invoice): Invoice = HibernateSession.createSession { s ->
        val i = InvoiceMapper.mapFrom(invoice)
        try {
            i.paymentMethod = PaymentMapper.mapFrom(paymentMethodService.get(invoice.paymentMethod!!))
        } catch (e: java.lang.IllegalStateException) {
            throw InvalidPaymentMethodException()
        }
        try {
            i.deliveryMethod = DeliveryMapper.mapFrom(deliveryMethodService.get(invoice.deliveryMethod!!))
        } catch (e: java.lang.IllegalStateException) {
            throw InvalidDeliveryMethodException()
        }

        s.save(i.person.address)
        s.save(i.person)
        if (i.company != null) {
            s.save(i.company!!.address)
            s.save(i.company)
        }

        val products = productService.getMultiple(invoice.products.map { it.productId })
        val invalidProducts = mutableListOf<Long>()

        invoice.products.forEach { p ->
            val foundProduct = products.find { it.id == p.productId }
            if (foundProduct == null || foundProduct.unitsOnStock < p.unitsCount)
                invalidProducts += p.productId
        }
        if (invalidProducts.isNotEmpty())
            throw InvalidProductException(invalidProducts)


        invoice.products.forEach {
            val tmp = s.saveAndGet(DbInvoiceProduct(null, it.unitsCount, null))
            tmp.product = ProductMapper.mapFrom(products.find { p -> p.id == it.productId }!!)
            s.update(tmp)
            i.products += tmp
        }
        invoice.products.forEach {
            productService.removeUnitsFromStock(it.productId, it.unitsCount)
        }

        val today = LocalDate.now()
        i.variableSymbol = "${today.year.toString().substring(2, 4)}${today.monthValue.toString().padStart(2, '0')}${today.dayOfMonth.toString().padStart(2, '0')}" + todaysCount().toString().padStart(4, '0')
        InvoiceMapper.mapTo(s.saveAndGet(i))
    }

    private fun todaysCount(): Long = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(Long::class.java)
        val root = cq.from(DbInvoice::class.java)

        val idPath: Path<DbInvoice> = root.get("created")
        cq.where(cb.equal(idPath, LocalDate.now()))

        s.createQuery(cq.select(cb.count(root))).singleResult
    }
}