package cz.kodytek.logic.mappers

import cz.kodytek.logic.mappers.interfaces.IMapper
import cz.kodytek.logic.models.invoice.DeliveryMethod
import cz.kodytek.eshop.data.entities.DeliveryMethod as DbDeliverMethod

object DeliveryMapper : IMapper<DbDeliverMethod, DeliveryMethod> {

    override fun mapTo(value: DbDeliverMethod): DeliveryMethod = DeliveryMethod(value.id, value.name, value.price)

    override fun mapFrom(value: DeliveryMethod): DbDeliverMethod = DbDeliverMethod(value.id, value.name, value.price)

}