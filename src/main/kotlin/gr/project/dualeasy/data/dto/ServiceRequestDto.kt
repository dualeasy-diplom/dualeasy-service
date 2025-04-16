package gr.project.dualeasy.data.dto

import gr.project.dualeasy.data.model.Service
import java.math.BigDecimal

class ServiceRequestDto(
    val name: String,
    val description: String,
    val address: String? = null,
    val mainPhoto: String? = null,
    val price: BigDecimal,
    val categoryId: Long? = null,
)

class GetServicesRequestDto(
    val ids: List<Long>,
)

fun ServiceRequestDto.toService(clientId: String) =
    Service(
        clientId = clientId,
        name = name,
        description = description,
        address = address,
        mainPhoto = mainPhoto,
        price = price,
        categoryId = categoryId,
    )
