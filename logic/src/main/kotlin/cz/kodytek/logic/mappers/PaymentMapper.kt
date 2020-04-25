package cz.kodytek.logic.mappers

import cz.kodytek.logic.mappers.interfaces.IMapper
import cz.kodytek.logic.models.invoice.PaymentMethod
import cz.kodytek.eshop.data.entities.PaymentMethod as DbPaymentMethod

object PaymentMapper : IMapper<DbPaymentMethod, PaymentMethod> {

    override fun mapTo(value: DbPaymentMethod): PaymentMethod = PaymentMethod(value.id, value.name)

    override fun mapFrom(value: PaymentMethod): cz.kodytek.eshop.data.entities.PaymentMethod = DbPaymentMethod(value.id, value.name)

}