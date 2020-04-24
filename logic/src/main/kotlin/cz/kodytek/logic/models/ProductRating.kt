package cz.kodytek.logic.models

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProductRating(
        val id: Long?,
        @get:Size(min = 3, max = 50)
        val fullName: String?,
        @get:Min(0)
        @get:Max(100)
        @get:NotNull
        val percent: Int,
        @get:Size(max = 255)
        val description: String?
) {
        constructor() : this(null, null, -1, null)
}