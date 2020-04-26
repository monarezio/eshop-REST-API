package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.eshop.data.entities.Product
import cz.kodytek.logic.mappers.PaymentMapper
import cz.kodytek.logic.models.invoice.PaymentMethod
import cz.kodytek.logic.services.interfaces.IPaymentMethodService
import javax.enterprise.context.ApplicationScoped
import javax.persistence.NoResultException
import cz.kodytek.eshop.data.entities.PaymentMethod as DbPaymentMethod

@ApplicationScoped
open class PaymentMethodService : IPaymentMethodService {

    override fun create(paymentMethod: PaymentMethod): PaymentMethod = HibernateSession.createSession { s ->
        PaymentMapper.mapTo(s.saveAndGet(PaymentMapper.mapFrom(paymentMethod)))
    }

    override fun getAll(): List<PaymentMethod> = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(DbPaymentMethod::class.java)
        val root = cq.from(DbPaymentMethod::class.java)
        try {
            s.createQuery(cq).resultList.map { PaymentMapper.mapTo(it) }
        } catch(e: NoResultException) {
            listOf()
        }
    }

    override fun get(id: Long): PaymentMethod = HibernateSession.createSession { s ->
        PaymentMapper.mapTo(s.get(DbPaymentMethod::class.java, id))
    }
}