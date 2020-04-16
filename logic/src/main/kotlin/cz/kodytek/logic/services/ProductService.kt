package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.eshop.data.entities.Category as DbCategory
import cz.kodytek.logic.mappers.ProductMapper
import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.Product
import cz.kodytek.logic.services.interfaces.IProductService
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
open class ProductService : IProductService {
    override fun create(product: Product, category: Category): Product = HibernateSession.createSession { s ->
        val c = s.get(DbCategory::class.java, category.id)
        val p = s.saveAndGet(ProductMapper.mapFrom(product))
        c.products.add(p)

        ProductMapper.mapTo(p)
    }
}
