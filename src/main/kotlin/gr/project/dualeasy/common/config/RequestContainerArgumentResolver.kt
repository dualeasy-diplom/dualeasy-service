package gr.project.dualeasy.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import gr.project.dualeasy.common.model.RequestContainer
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.lang.reflect.ParameterizedType

@Component
class RequestContainerArgumentResolver(
    private val objectMapper: ObjectMapper,
    private val httpServletRequest: HttpServletRequest,
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean = parameter.parameterType == RequestContainer::class.java

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val bodyBytes = httpServletRequest.inputStream.readBytes()

        val genericType =
            parameter.genericParameterType as? ParameterizedType
                ?: throw IllegalStateException("RequestContainer must be parameterized")

        val tType = genericType.actualTypeArguments[0]
        val javaType = TypeFactory.defaultInstance().constructType(tType)

        val body =
            try {
                objectMapper.readValue(bodyBytes, javaType) as Any
            } catch (e: Exception) {
                throw IllegalArgumentException("💥 Failed to parse request body as $tType", e)
            }

        val clientId = httpServletRequest.getHeader("X-Client-Id")
        val role = httpServletRequest.getHeader("X-Role")

        return RequestContainer(
            request = body,
            clientId = clientId,
            role = role,
        )
    }
}
