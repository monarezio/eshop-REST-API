package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.logic.mappers.CategoryMapper
import cz.kodytek.logic.mappers.ProductMapper
import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.CategoryPage
import cz.kodytek.logic.services.interfaces.ICategoryService
import javax.enterprise.context.ApplicationScoped
import javax.persistence.criteria.Fetch
import javax.persistence.criteria.Join
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Path
import cz.kodytek.eshop.data.entities.Category as DbCategory
import cz.kodytek.eshop.data.entities.Product as DbProduct

@ApplicationScoped
open class CategoryService : ICategoryService {

    private val perPage = 8

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

    override fun getDetail(id: Long, page: Int): CategoryPage = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(DbCategory::class.java)
        val root = cq.from(DbCategory::class.java)

        val idPath: Path<DbCategory> = root.get("id")
        cq.where(cb.equal(idPath, id))

        //val fetch: Fetch<DbCategory, DbProduct> = root.fetch("products", JoinType.LEFT)

        println("PAGE: " + page)
        println("PERPAGE: " + perPage)

        val query = s.createQuery(cq)
        query.maxResults = perPage
        query.firstResult = page

        val c = query.singleResult
        val cCount = count(id).toInt()

        CategoryPage(
                CategoryMapper.mapTo(c),
                page, cCount / perPage,
                c.products.map { ProductMapper.mapTo(it) }
        )
    }

    private fun count(categoryId: Long): Long = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(Long::class.java)
        val root = cq.from(DbCategory::class.java)

        val join: Join<DbCategory, DbProduct> = root.join("products", JoinType.LEFT)

        val idPath: Path<DbCategory> = root.get("id")
        cq.where(cb.equal(idPath, categoryId))

        s.createQuery(cq.select(cb.count(root))).singleResult
    }

}
