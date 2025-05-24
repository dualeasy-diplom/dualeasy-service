package gr.project.dualeasy.data.dto

import gr.project.dualeasy.data.model.Service
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal

class ServiceRequestDto(
    val name: String,
    val description: String,
    val address: String? = null,
    val price: BigDecimal,
    val categoryId: Long? = null,
    val mainPhoto: MultipartFile?,
)

class GetServicesRequestDto(
    val ids: List<Long>,
)

fun ServiceRequestDto.toService(
    clientId: String,
    imageUrl: String? = null,
) = Service(
    clientId = clientId,
    name = name,
    description = description,
    address = address,
    mainPhoto = imageUrl,
    price = price,
    categoryId = categoryId,
)
