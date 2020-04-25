package cz.kodytek.logic.models.invoice

import javax.validation.constraints.NotNull

class Address {

    @get:NotNull
    var city: String? = null
    @get:NotNull
    var street: String? = null
    @get:NotNull
    var postcode: String? = null

    constructor(city: String, street: String, postcode: String) {
        this.city = city
        this.street = street
        this.postcode = postcode
    }

    constructor()
}