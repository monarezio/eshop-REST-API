package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class InvoiceProduct(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var unitCount: Int,
        @ManyToOne
        var product: Product?
)