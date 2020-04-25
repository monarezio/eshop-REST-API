package cz.kodytek.logic.services.interfaces

import cz.kodytek.logic.models.invoice.PaymentMethod

interface IPaymentMethodService {

    fun create(paymentMethod: PaymentMethod): PaymentMethod

    fun getAll(): List<PaymentMethod>

    fun get(id: Long): PaymentMethod

}