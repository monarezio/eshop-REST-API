package cz.kodytek.eshop.data.entities

import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
data class Category(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false, unique = true)
        var name: String,
        @Column(nullable = false)
        @Type(type = "text")
        var description: String,
        @OneToMany
        @JoinColumn(name = "category_id")
        var products: List<Product>
)
