package cz.kodytek.logic.models

import javax.validation.constraints.NotNull

data class Image(
        val miniaturePath: String?,
        @get:NotNull
        val path: String
) {
    constructor(path: String) : this(null, path)
}
