package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class Company(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,
        @Column(nullable = false)
        var name: String,
        @Column(nullable = false)
        var identificationNumber: String,
        @Column(nullable = true)
        var taxIdentificationNumber: String?,
        @OneToOne
        var address: Address
)