package cz.kodytek.logic.models

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class ProductRating {
    var id: Long? = null

    @get:Size(min = 3, max = 50)
    var fullName: String? = null

    @get:Min(0)
    @get:Max(100)
    @get:NotNull
    var percent: Int = -1

    @get:Size(max = 255)
    var description: String? = null

    constructor(id: Long?,
                fullName: String?,
                percent: Int,
                description: String?) {
        this.id = id
        this.fullName = fullName
        this.percent = percent
        this.description = description
    }

    constructor()
}