package cz.kodytek.logic.mappers

import cz.kodytek.logic.mappers.interfaces.IProductMapper
import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.Image
import cz.kodytek.logic.models.Product
import cz.kodytek.logic.models.ProductRating
import cz.kodytek.eshop.data.entities.Product as DbProduct
import cz.kodytek.eshop.data.entities.Category as DbCategory

object ProductMapper : IProductMapper {
    override fun mapTo(value: DbProduct): Product = Product(
            value.id, value.title, value.description, value.unitsOnStock,
            value.price, value.images.map { Image(it.miniaturePath, it.path) },
            value.parameters.map { Pair(it.name, it.value) }.toMap(),
            value.ratings.map { ProductRating(it.id, it.fullName, it.percent, it.description) }
    )

    override fun mapFrom(value: Product, category: Category): DbProduct = DbProduct(
            value.id, value.title, value.description, value.unitsOnStock,
            value.price, mutableSetOf(), mutableSetOf(), DbCategory(category.id, "", "", mutableSetOf())
    )

    override fun mapFrom(value: Product): cz.kodytek.eshop.data.entities.Product = DbProduct(
            value.id, value.title, value.description, value.unitsOnStock,
            value.price, mutableSetOf(), mutableSetOf()
    )
}
