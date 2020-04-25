package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class PaymentMethod(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var name: String
)