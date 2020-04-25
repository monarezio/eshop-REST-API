package cz.kodytek.logic.models.invoice

import cz.kodytek.logic.models.Product
import net.bytebuddy.implementation.bind.annotation.Empty
import javax.validation.constraints.Min
import javax.validation.constraints.Null

class InvoiceProduct {
    @get:Min(1)
    var productId: Long = 0
    @get:Min(1)
    var unitsCount: Int = 0
    @get:Null
    var product: Product? = null

    constructor(productId: Long, unitsCount: Int) {
        this.productId = productId
        this.unitsCount = unitsCount
    }

    constructor(unitsCount: Int, product: Product) {
        this.productId = product.id!!
        this.unitsCount = unitsCount
        this.product = product
    }

    constructor()

}