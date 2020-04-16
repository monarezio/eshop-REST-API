package cz.kodytek.logic.mappers.interfaces

import cz.kodytek.logic.models.Category
import cz.kodytek.eshop.data.entities.Category as DbCategory

interface ICategoryMapper : IMapper<DbCategory, Category>
