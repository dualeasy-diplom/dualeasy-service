package gr.project.dualeasy.data.kafka

import gr.project.dualeasy.data.model.Service
import java.math.BigDecimal

data class SearchRequest(
    val id: Long,
    val name: String,
    val description: String,
    val rating: BigDecimal?,
    val price: BigDecimal,
)

fun Service.toSearchRequest() =
    SearchRequest(
        id = id,
        name = name,
        description = description,
        rating = rating,
        price = price,
    )
