package cz.kodytek.logic.services.interfaces

import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.Product

interface IProductService {

    fun create(product: Product, category: Category): Product

}
