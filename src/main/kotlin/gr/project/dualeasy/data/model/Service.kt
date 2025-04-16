package gr.project.dualeasy.data.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "service")
data class Service(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var clientId: String,
    var name: String,
    var description: String,
    var address: String? = null,
    var mainPhoto: String? = null,
    var rating: BigDecimal? = null,
    var price: BigDecimal,
    var categoryId: Long? = null,
)
