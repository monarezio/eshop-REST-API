package cz.kodytek.eshop.data.entities

import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
class Product(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var title: String,
        @Column(nullable = false)
        @Type(type = "text")
        var description: String,
        @Column(nullable = false)
        var unitsOnStock: Int,
        @Column(nullable = false)
        var price: Int,
        @OneToMany
        @JoinColumn(name = "product_id")
        var images: MutableSet<Image>,
        @OneToMany
        @JoinColumn(name = "product_id")
        var parameters: MutableSet<ProductParameter>,
        @ManyToOne(fetch = FetchType.LAZY)
        var category: Category? = null,
        @OneToMany
        @JoinColumn(name = "product_id")
        var ratings: MutableSet<ProductRating> = mutableSetOf()
) {
    constructor(id: Long) : this(id, "", "", 0, 0, mutableSetOf(), mutableSetOf())
}
