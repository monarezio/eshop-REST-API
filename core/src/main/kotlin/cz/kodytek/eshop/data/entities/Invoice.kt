package cz.kodytek.eshop.data.entities

import java.time.LocalDate
import javax.persistence.*

@Entity
class Invoice(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false, unique = true)
        var variableSymbol: String,
        @OneToOne
        var person: Person,
        @OneToOne
        var company: Company?,
        @ManyToOne
        var paymentMethod: PaymentMethod?,
        @ManyToOne
        var deliveryMethod: DeliveryMethod?,
        @OneToMany
        var products: Set<InvoiceProduct>,
        @Column
        var note: String?,
        @Column
        var created: LocalDate
)