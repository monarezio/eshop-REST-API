package cz.kodytek.eshop.data.entities

import org.hibernate.annotations.Type
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Log(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var url: String,
        @Column(nullable = false)
        @Type(type = "text")
        var body: String,
        @Column(nullable = false)
        var ip: String,
        @Column(nullable = false)
        var datetime: LocalDateTime
)