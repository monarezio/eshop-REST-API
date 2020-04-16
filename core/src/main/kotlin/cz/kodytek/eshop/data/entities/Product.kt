package cz.kodytek.eshop.data.entities

import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
data class Product(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var title: String,
        @Column(nullable = false)
        @Type(type = "text")
        var description: String,
        @OneToMany
        @JoinColumn(name = "product_id")
        var images: List<Image>
)
