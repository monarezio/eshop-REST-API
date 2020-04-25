package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.logic.mappers.DeliveryMapper
import cz.kodytek.logic.models.invoice.DeliveryMethod
import cz.kodytek.logic.services.interfaces.IDeliveryMethodService
import javax.enterprise.context.ApplicationScoped
import javax.persistence.NoResultException
import cz.kodytek.eshop.data.entities.DeliveryMethod as DbDeliveryMethod

@ApplicationScoped
class DeliveryMethodService : IDeliveryMethodService {

    override fun create(deliveryMethod: DeliveryMethod): DeliveryMethod = HibernateSession.createSession { s ->
        DeliveryMapper.mapTo(s.saveAndGet(DeliveryMapper.mapFrom(deliveryMethod)))
    }

    override fun getAll(): List<DeliveryMethod> = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(DbDeliveryMethod::class.java)
        val root = cq.from(DbDeliveryMethod::class.java)
        try {
            s.createQuery(cq).resultList.map { DeliveryMapper.mapTo(it) }
        } catch (e: NoResultException) {
            listOf()
        }
    }

    override fun get(id: Long): DeliveryMethod = HibernateSession.createSession { s ->
        DeliveryMapper.mapTo(s.get(DbDeliveryMethod::class.java, id))
    }
}