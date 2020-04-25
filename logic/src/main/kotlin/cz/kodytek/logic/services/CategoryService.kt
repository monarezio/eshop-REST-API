package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.eshop.data.entities.Image
import cz.kodytek.eshop.data.entities.ProductParameter
import cz.kodytek.eshop.data.entities.ProductRating
import cz.kodytek.logic.mappers.CategoryMapper
import cz.kodytek.logic.mappers.ProductMapper
import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.CategoryPage
import cz.kodytek.logic.models.Order
import cz.kodytek.logic.models.ProductOrderBy
import cz.kodytek.logic.services.interfaces.ICategoryService
import javax.enterprise.context.ApplicationScoped
import javax.persistence.NoResultException
import javax.persistence.criteria.*
import cz.kodytek.eshop.data.entities.Category as DbCategory
import cz.kodytek.eshop.data.entities.Product as DbProduct
import cz.kodytek.eshop.data.entities.ProductParameter as DbProductParameter

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

    override fun getDetail(id: Long, page: Int, search: String, productOrderBy: ProductOrderBy, order: Order): CategoryPage = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(DbCategory::class.java)
        val root = cq.from(DbCategory::class.java)

        val idPath: Path<DbCategory> = root.get("id")
        cq.where(cb.equal(idPath, id))

        val products = try {
            val cbp = s.criteriaBuilder
            val productsQuery = cbp.createQuery(DbProduct::class.java)
            val productsRoot = productsQuery.from(DbProduct::class.java)

            val join: Join<DbProduct, DbCategory> = productsRoot.join("category", JoinType.LEFT)
            val joinParams: Join<DbProduct, DbProductParameter> = productsRoot.join("parameters", JoinType.LEFT)

            val fetchParameters: Fetch<DbProduct, ProductParameter> = productsRoot.fetch("parameters", JoinType.LEFT)
            val fetchRatings: Fetch<DbProduct, ProductRating> = productsRoot.fetch("ratings", JoinType.LEFT)
            val fetchImages: Fetch<DbProduct, Image> = productsRoot.fetch("images", JoinType.LEFT)

            val categoryId: Path<DbCategory> = join.get("id")
            val productTitle: Expression<String> = productsRoot.get("title")
            val productDesc: Expression<String> = productsRoot.get("description")
            val productParams: Expression<String> = joinParams.get("value")
            productsQuery.where(
                    cbp.and(
                            cbp.equal(categoryId, id),
                            cbp.or(
                                    cbp.like(productTitle, "%$search%"),
                                    cbp.like(productDesc, "%$search%"),
                                    cbp.like(productParams, "%$search%")
                            )
                    )
            )

            val path: Path<DbProduct> = when (productOrderBy) {
                ProductOrderBy.id -> {
                    productsRoot.get("id")
                }
                ProductOrderBy.price -> {
                    productsRoot.get("price")
                }
                ProductOrderBy.title -> {
                    productsRoot.get("title")
                }
                else -> {
                    productsRoot.get("unitsOnStock")
                }
            }
            val orderDirection = when (order) {
                Order.asc -> cbp.asc(path)
                else -> cbp.desc(path)
            }
            productsQuery.orderBy(orderDirection)

            val query = s.createQuery(productsQuery)
            query.maxResults = perPage
            query.firstResult = page * perPage
            query.resultList
        } catch (e: NoResultException) {
            mutableListOf<DbProduct>()
        }

        val c = s.createQuery(cq).singleResult
        val cCount = count(id).toInt()

        CategoryPage(
                CategoryMapper.mapTo(c),
                page, cCount / perPage,
                products.map { ProductMapper.mapTo(it) }
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
