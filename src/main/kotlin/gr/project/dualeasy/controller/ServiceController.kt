package gr.project.dualeasy.controller

import gr.project.dualeasy.common.model.RequestContainer
import gr.project.dualeasy.data.dto.GetServicesRequestDto
import gr.project.dualeasy.data.dto.ServiceRequestDto
import gr.project.dualeasy.data.dto.toService
import gr.project.dualeasy.data.model.Service
import gr.project.dualeasy.service.ServiceService
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
class ServiceController(
    private val serviceService: ServiceService,
) {
    @PostMapping("/create")
    fun createService(container: RequestContainer<ServiceRequestDto>): ResponseEntity<Service> {
        val created =
            serviceService.createService(
                container.request.toService(clientId = container.clientId!!),
            )
        return ResponseEntity.ok(created)
    }

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

    @DeleteMapping("/{id}")
    fun deleteService(
        @PathVariable id: Long,
        @RequestHeader("X-Client-Id") clientId: String,
    ): ResponseEntity<Void> {
        serviceService.deleteService(id, clientId)
        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun getAllServices(): List<Service> = serviceService.getAll()

    @PostMapping
    fun getServicesByIds(
        @RequestBody request: GetServicesRequestDto,
    ): List<Service> = serviceService.getByIds(request.ids)

    @GetMapping("/service/{id}")
    fun getServiceById(
        @PathVariable id: Long,
    ): Service = serviceService.getById(id)

    @GetMapping("/service/client/my")
    fun getMyServices(
        @RequestHeader("X-Client-Id") clientId: String,
    ) = serviceService.getAllByClientId(clientId)

    @GetMapping("/service/client/{clientId}")
    fun getServiceByClientId(
        @PathVariable clientId: String,
    ) = serviceService.getAllByClientId(clientId).also { println(1) }
}
