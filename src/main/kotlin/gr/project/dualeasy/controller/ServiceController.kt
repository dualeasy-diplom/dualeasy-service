package gr.project.dualeasy.controller

import gr.project.dualeasy.common.model.RequestContainer
import gr.project.dualeasy.data.dto.GetServicesRequestDto
import gr.project.dualeasy.data.dto.ServiceRequestDto
import gr.project.dualeasy.data.dto.toService
import gr.project.dualeasy.data.model.Service
import gr.project.dualeasy.service.ServiceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/services")
@Tag(name = "Services", description = "Управление сервисами")
class ServiceController(
    private val serviceService: ServiceService,
) {
    @Operation(summary = "Создать сервис")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Сервис создан"),
        ApiResponse(responseCode = "400", description = "Неверный запрос"),
    )
    @PostMapping("/create")
    fun createService(
        container: RequestContainer<ServiceRequestDto>,
    ): ResponseEntity<Service> {
        val created =
            serviceService.createService(
                container.request.toService(clientId = container.clientId!!),
            )
        return ResponseEntity.ok(created)
    }

    @Operation(summary = "Обновить сервис")
    @PutMapping("/edit/{serviceId}")
    fun updateService(
        container: RequestContainer<ServiceRequestDto>,
        @PathVariable serviceId: Long,
    ): ResponseEntity<Service> =
        ResponseEntity.ok(
            serviceService.updateService(
                container.request.toService(clientId = container.clientId!!),
                serviceId,
            ),
        )

    @Operation(summary = "Удалить сервис")
    @DeleteMapping("/{id}")
    fun deleteService(
        @PathVariable id: Long,
        @RequestHeader("X-Client-Id") clientId: String,
    ): ResponseEntity<Unit> {
        serviceService.deleteService(id, clientId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "Получить все сервисы")
    @GetMapping
    fun getAllServices(): List<Service> = serviceService.getAll()

    @Operation(summary = "Получить сервисы по списку ID")
    @PostMapping
    fun getServicesByIds(
        @RequestBody request: GetServicesRequestDto,
    ): List<Service> = serviceService.getByIds(request.ids)

    @Operation(summary = "Получить сервис по ID")
    @GetMapping("/service/{id}")
    fun getServiceById(
        @PathVariable id: Long,
    ): Service = serviceService.getById(id)

    @Operation(summary = "Получить свои сервисы")
    @GetMapping("/service/client/my")
    fun getMyServices(
        @RequestHeader("X-Client-Id") clientId: String,
    ) = serviceService.getAllByClientId(clientId)

    @Operation(summary = "Получить сервисы клиента по ID клиента")
    @GetMapping("/service/client/{clientId}")
    fun getServiceByClientId(
        @PathVariable clientId: String,
    ) = serviceService.getAllByClientId(clientId)
}
