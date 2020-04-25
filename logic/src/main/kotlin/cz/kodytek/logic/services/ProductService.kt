package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.eshop.data.entities.Image
import cz.kodytek.eshop.data.entities.ProductParameter
import cz.kodytek.logic.mappers.ProductMapper
import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.Product
import cz.kodytek.logic.models.ProductRating
import cz.kodytek.logic.services.interfaces.IProductService
import net.coobird.thumbnailator.Thumbnails
import org.hibernate.Session
import java.io.File
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.imageio.ImageIO
import javax.persistence.criteria.Fetch
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Path
import cz.kodytek.eshop.data.entities.Product as DbProduct
import cz.kodytek.eshop.data.entities.ProductParameter as DbProductParameter
import cz.kodytek.eshop.data.entities.ProductRating as DbProductRating

@ApplicationScoped
open class ProductService : IProductService {
    override fun create(product: Product, category: Category): Product = HibernateSession.createSession { s ->
        val classLoader = Thread.currentThread().contextClassLoader

        File("resources/products").mkdirs()

        val p = ProductMapper.mapFrom(product, category)

        for (image in product.images) {
            val uuid = UUID.randomUUID()
            val miniaturePath = "assets/products/${uuid}_min.jpg"
            val path = "assets/products/$uuid.jpg"

            println("Image path: " + image.path)

            val originalImage = ImageIO.read(
                    classLoader.getResourceAsStream(image.path)
            )

            ImageIO.write(originalImage, "jpg", File(path.replace("assets/", "resources/")))

            Thumbnails.of(originalImage)
                    .outputFormat("jpg")
                    .size(500, 500)
                    .toFile(File(miniaturePath.replace("assets/", "resources/")))

            val i = s.saveAndGet(Image(null, miniaturePath, path))
            p.images.add(i)
        }

        for (param in product.parameters) {
            p.parameters.add(s.saveAndGet(ProductParameter(null, param.key, param.value)))
        }

        for (rating in product.ratings) {
            p.ratings.add(s.saveAndGet(DbProductRating(null, rating.fullName, rating.percent, rating.description)))
        }

        s.save(p)

        ProductMapper.mapTo(p)
    }

    override fun getDetail(id: Long): Product = HibernateSession.createSession { s ->
        ProductMapper.mapTo(getRawProduct(s, id))
    }

    override fun getMultiple(ids: List<Long>): List<Product> = HibernateSession.createSession { s ->
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(DbProduct::class.java)
        val root = cq.from(DbProduct::class.java)

        val idPath: Path<DbProduct> = root.get("id")
        cq.where(idPath.`in`(ids))

        val fetchParameters: Fetch<DbProduct, DbProductParameter> = root.fetch("parameters", JoinType.LEFT)
        val fetchRatings: Fetch<DbProduct, DbProductRating> = root.fetch("ratings", JoinType.LEFT)
        val fetchImages: Fetch<DbProduct, Image> = root.fetch("images", JoinType.LEFT)

        s.createQuery(cq).resultList.map { ProductMapper.mapTo(it) }
    }

    override fun removeUnitsFromStock(id: Long, units: Int) = HibernateSession.createSession { s ->
        val p = s.get(DbProduct::class.java, id)
        p.unitsOnStock -= units
        s.update(p)
    }

    override fun rate(productId: Long, rating: ProductRating): ProductRating = HibernateSession.createSession { s ->
        val product = getRawProduct(s, productId)
        val r = s.saveAndGet(DbProductRating(null, rating.fullName, rating.percent, rating.description))
        product.ratings.add(r)

        ProductRating(r.id, r.fullName, r.percent, r.description)
    }

    private fun getRawProduct(s: Session, id: Long): DbProduct {
        val cb = s.criteriaBuilder

        val cq = cb.createQuery(DbProduct::class.java)
        val root = cq.from(DbProduct::class.java)

        val idPath: Path<DbProduct> = root.get("id")
        cq.where(cb.equal(idPath, id))

        val fetchParameters: Fetch<DbProduct, DbProductParameter> = root.fetch("parameters", JoinType.LEFT)
        val fetchRatings: Fetch<DbProduct, DbProductRating> = root.fetch("ratings", JoinType.LEFT)

        return s.createQuery(cq).singleResult
    }
}