package cz.kodytek.eshop.data.entities

import javax.persistence.*

@Entity
class ApiAccessKey(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?,
        @Column(nullable = false)
        var email: String,
        @Column(nullable = false, unique = true)
        var token: String,
        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "key_id")
        var logs: MutableSet<Log> = mutableSetOf()
)