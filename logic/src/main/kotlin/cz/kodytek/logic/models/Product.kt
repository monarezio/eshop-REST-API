package cz.kodytek.logic.models

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class Product(
        val id: Long?,
        @get:Size(min = 3)
        @get:NotNull
        val title: String,
        @get:Size(min = 10)
        @get:NotNull
        val description: String,
        @get:NotNull
        val unitsOnStock: Int,
        @get:NotNull
        val price: Int,
        val images: List<Image>,
        val parameters: Map<String, String>,
        val ratings: List<ProductRating>
)
