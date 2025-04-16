package gr.project.dualeasy.common.model

data class RequestContainer<T>(
    val request: T,
    val clientId: String?,
    val role: String?,
)
