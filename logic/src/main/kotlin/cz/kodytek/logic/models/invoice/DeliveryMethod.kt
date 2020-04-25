package cz.kodytek.logic.models.invoice

data class DeliveryMethod(
        val id: Long? = null,
        val name: String,
        val price: Int
)