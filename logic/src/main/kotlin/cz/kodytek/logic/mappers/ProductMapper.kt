package cz.kodytek.logic.mappers

import cz.kodytek.eshop.data.entities.ProductParameter
import cz.kodytek.logic.mappers.interfaces.IProductMapper
import cz.kodytek.logic.models.Image
import cz.kodytek.logic.models.Product
import cz.kodytek.eshop.data.entities.Image as DbImage
import cz.kodytek.eshop.data.entities.Product as DbProduct

object ProductMapper : IProductMapper {
    override fun mapTo(value: DbProduct): Product = Product(
            value.id, value.title, value.description, value.unitsOnStock,
            value.price, value.images.map { Image(it.miniaturePath, it.path) },
            value.parameters.map { Pair(it.name, it.value) }.toMap()
    )

    override fun mapFrom(value: Product): DbProduct = DbProduct(
            value.id, value.title, value.description, value.unitsOnStock,
            value.price, value.images.map { DbImage(null, it.miniaturePath, it.path) },
            value.parameters.map { ProductParameter(null, it.key, it.value) }
    )
}
