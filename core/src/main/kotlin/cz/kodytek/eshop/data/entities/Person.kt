package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class Person(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var fullName: String,
        @OneToOne
        var address: Address
)