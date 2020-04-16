package cz.kodytek.logic.services.interfaces

import cz.kodytek.logic.models.Category

interface ICategoryService {

    fun getAll(): List<Category>

    fun create(c: Category): Category

}
