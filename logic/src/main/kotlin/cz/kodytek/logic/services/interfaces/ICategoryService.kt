package cz.kodytek.logic.services.interfaces

import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.CategoryPage
import cz.kodytek.logic.models.Order
import cz.kodytek.logic.models.ProductOrderBy
import javax.persistence.NoResultException

interface ICategoryService {

    fun getAll(): List<Category>

    fun create(c: Category): Category

    @Throws(NoResultException::class)
    fun getDetail(id: Long, page: Int, search: String, productOrderBy: ProductOrderBy, order: Order): CategoryPage

}
