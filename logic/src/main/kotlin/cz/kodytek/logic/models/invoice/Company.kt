package cz.kodytek.logic.models.invoice

import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class Company {

    @get:Size(min = 3)
    @get:NotNull
    var name: String? = null
    @get:NotNull
    var identificationNumber: String? = null
    var taxIdentificationNumber: String? = null
    @get:NotNull
    @get:Valid
    var address: Address? = null

    constructor(name: String?, identificationNumber: String?, taxIdentificationNumber: String?, address: Address?) {
        this.name = name
        this.identificationNumber = identificationNumber
        this.taxIdentificationNumber = taxIdentificationNumber
        this.address = address
    }

    constructor()

}