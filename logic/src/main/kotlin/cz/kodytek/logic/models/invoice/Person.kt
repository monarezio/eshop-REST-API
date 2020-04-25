package cz.kodytek.logic.models.invoice

import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class Person {

    @get:Size(min = 3, max = 50)
    @get:NotNull
    var fullName: String? = null
    @get:NotNull
    @get:Valid
    var address: Address? = null

    constructor(fullName: String?, address: Address?) {
        this.fullName = fullName
        this.address = address
    }

    constructor()

}