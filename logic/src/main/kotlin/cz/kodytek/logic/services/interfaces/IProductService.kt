package cz.kodytek.logic.services.interfaces

import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.Product
import cz.kodytek.logic.models.ProductRating

interface IProductService {

    fun create(product: Product, category: Category): Product

    fun getDetail(id: Long): Product

    fun rate(id: Long, rating: ProductRating): ProductRating

}
