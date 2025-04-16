package gr.project.dualeasy.data.dto

import java.math.BigDecimal

data class RatingUpdateEvent(
    val serviceId: Long,
    val averageScore: BigDecimal,
)