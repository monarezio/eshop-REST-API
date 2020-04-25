package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class DeliveryMethod(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @Column(nullable = false)
        var name: String,
        @Column(nullable = false)
        var price: Int
)