package cz.kodytek.logic.mappers.interfaces

import cz.kodytek.logic.models.Product
import cz.kodytek.eshop.data.entities.Product as DbProduct

interface IProductMapper : IMapper<DbProduct, Product>
