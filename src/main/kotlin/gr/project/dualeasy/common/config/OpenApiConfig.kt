package gr.project.dualeasy.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI =
        OpenAPI()
            .info(
                Info()
                    .title("Service API")
                    .version("1.0")
                    .description("API для управления сервисами"),
            )
}
@Controller
internal class SwaggerUIController {
    @RequestMapping("/")
    fun index(): String = "redirect:/swagger-ui/index.html"
}