package gr.project.dualeasy.common.config

import gr.project.dualeasy.common.ApiException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {
    @ExceptionHandler(ApiException::class)
    fun handleApiException(e: ApiException): ResponseEntity<Map<String, Any>> =
        ResponseEntity
            .status(HttpStatus.resolve(e.code) ?: HttpStatus.BAD_REQUEST)
            .body(
                mapOf(
                    "code" to e.code,
                    "message" to e.message.orEmpty(),
                ),
            )
}
