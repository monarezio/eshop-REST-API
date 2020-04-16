package cz.kodytek.logic.models

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class Category(
        val id: Long?,
        @get:Size(min = 3)
        @get:NotNull
        val name: String,
        @get:Size(min = 10)
        @get:NotNull
        val description: String
) {
    constructor(name: String, description: String): this(null, name, description)
}
