package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.logic.mappers.CategoryMapper
import cz.kodytek.logic.models.Category
import cz.kodytek.logic.services.interfaces.ICategoryService
import javax.enterprise.context.ApplicationScoped
import cz.kodytek.eshop.data.entities.Category as DbCategory

@ApplicationScoped
open class CategoryService : ICategoryService {

    override fun getAll(): List<Category> = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder
        val cq = cb.createQuery(DbCategory::class.java)
        val root = cq.from(DbCategory::class.java)

        s.createQuery(cq).resultList.map { CategoryMapper.mapTo(it) }
    }

    override fun create(c: Category): Category = HibernateSession.createSession { s ->
        CategoryMapper.mapTo(
                s.saveAndGet(CategoryMapper.mapFrom(c))
        )
    }

}
