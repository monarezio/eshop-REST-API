package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class ProductRating(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        @Column
        val fullName: String?,
        @Column(nullable = false)
        val percent: Int,
        @Column
        val description: String?
)