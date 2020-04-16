package cz.kodytek.logic.models

import javax.validation.constraints.NotNull

data class Image(
        @get:NotNull
        val miniaturePath: String,
        @get:NotNull
        val path: String
)
