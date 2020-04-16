package cz.kodytek.logic.mappers

import cz.kodytek.eshop.data.entities.Category as DbCategory
import cz.kodytek.logic.mappers.interfaces.ICategoryMapper
import cz.kodytek.logic.models.Category

object CategoryMapper : ICategoryMapper {

    override fun mapTo(value: DbCategory): Category = Category(value.id!!, value.name, value.description)

    override fun mapFrom(value: Category): DbCategory = DbCategory(null, value.name, value.description, listOf())

}
