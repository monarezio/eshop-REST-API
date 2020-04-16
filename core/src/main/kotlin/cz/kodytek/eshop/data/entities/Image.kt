package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
data class Image(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false, unique = true)
        var miniaturePath: String,
        @Column(nullable = false, unique = true)
        var path: String
)
