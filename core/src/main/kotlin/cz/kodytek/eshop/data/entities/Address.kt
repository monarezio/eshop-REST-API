package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class Address(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @Column(nullable = false)
        var city: String,
        @Column(nullable = false)
        var street: String,
        @Column(nullable = false)
        var postcode: String
)