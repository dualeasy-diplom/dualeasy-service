package gr.project.dualeasy.common

class ApiException(
    val code: Int,
    message: String,
) : RuntimeException(message) {
    companion object {
        val NOT_FOUND_EXCEPTION = ApiException(404, "Record not found")
        val PERMISSION_DENIED = ApiException(401, "Permission denied")
    }
}
