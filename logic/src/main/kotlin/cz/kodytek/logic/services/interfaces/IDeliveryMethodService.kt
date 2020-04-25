package cz.kodytek.logic.services.interfaces

import cz.kodytek.logic.models.invoice.DeliveryMethod

interface IDeliveryMethodService {

    fun create(deliveryMethod: DeliveryMethod): DeliveryMethod

    fun getAll(): List<DeliveryMethod>

    fun get(id: Long): DeliveryMethod

}