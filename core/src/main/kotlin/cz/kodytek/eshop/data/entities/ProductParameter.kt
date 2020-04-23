package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class ProductParameter(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var name: String,
        @Column(nullable = false)
        var value: String
)
