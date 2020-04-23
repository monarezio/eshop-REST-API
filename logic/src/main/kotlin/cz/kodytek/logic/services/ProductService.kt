package cz.kodytek.logic.services

import cz.kodytek.eshop.data.connections.HibernateSession
import cz.kodytek.eshop.data.connections.extensionns.saveAndGet
import cz.kodytek.eshop.data.entities.Image
import cz.kodytek.eshop.data.entities.ProductParameter
import cz.kodytek.logic.mappers.ProductMapper
import cz.kodytek.logic.models.Category
import cz.kodytek.logic.models.Product
import cz.kodytek.logic.services.interfaces.IProductService
import net.coobird.thumbnailator.Thumbnails
import java.io.File
import java.io.FileInputStream
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.imageio.ImageIO
import cz.kodytek.eshop.data.entities.Category as DbCategory

@ApplicationScoped
open class ProductService : IProductService {
    override fun create(product: Product, category: Category): Product = HibernateSession.createSession { s ->
        val classLoader = Thread.currentThread().contextClassLoader

        File("resources/products").mkdirs()

        val p = ProductMapper.mapFrom(product, category)

        println("Product: " + product)

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

        for(param in product.parameters) {
            p.parameters.add(s.saveAndGet(ProductParameter(null, param.key, param.value)))
        }

        s.save(p)

        ProductMapper.mapTo(p)
    }
}