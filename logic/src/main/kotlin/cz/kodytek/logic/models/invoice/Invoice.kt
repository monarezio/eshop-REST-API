package cz.kodytek.logic.models.invoice

import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Null
import javax.validation.constraints.Size

class Invoice {

    @get:Null
    var variableSymbol: String? = null

    @get:NotNull
    @get:Valid
    var person: Person? = null

    @get:Valid
    var company: Company? = null

    @get:NotNull
    var paymentMethod: Long? = null

    @get:NotNull
    var deliveryMethod: Long? = null

    @get:Valid
    @get:NotEmpty
    var products: List<InvoiceProduct> = listOf()

    @get:Size(min = 3)
    var note: String? = null

    constructor(variableSymbol: String?, person: Person?, company: Company?, paymentMethod: Long?, deliveryMethod: Long?, products: List<InvoiceProduct>, note: String?) {
        this.variableSymbol = variableSymbol
        this.person = person
        this.company = company
        this.paymentMethod = paymentMethod
        this.deliveryMethod = deliveryMethod
        this.products = products
        this.note = note
    }

    constructor()

}