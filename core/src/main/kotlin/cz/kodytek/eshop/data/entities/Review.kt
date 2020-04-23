package cz.kodytek.eshop.data.entities

import org.hibernate.annotations.Type
import javax.persistence.*

@Entity
class Review(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var name: String,
        @Column(nullable = false)
        @Type(type = "text")
        var body: String,
        @Column(nullable = false)
        var rating: Int
)
