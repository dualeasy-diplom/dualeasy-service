package gr.project.dualeasy.service

import gr.project.dualeasy.common.ApiException.Companion.NOT_FOUND_EXCEPTION
import gr.project.dualeasy.common.ApiException.Companion.PERMISSION_DENIED
import gr.project.dualeasy.data.kafka.toSearchRequest
import gr.project.dualeasy.data.model.Service
import gr.project.dualeasy.data.repository.ServiceRepository
import java.math.BigDecimal
import org.springframework.stereotype.Service as SpringService

@SpringService
class ServiceService(
    private val serviceRepository: ServiceRepository,
    private val kafkaService: KafkaService,
) {
    fun createService(service: Service): Service {
        val result = serviceRepository.save(service)
        kafkaService.sendToSearch(result.toSearchRequest())
        return result
    }

    fun updateService(
        serviceId: Long,
        rating: BigDecimal,
    ): Service {
        val service = serviceRepository.findById(serviceId).orElseThrow { NOT_FOUND_EXCEPTION }
        service.rating = rating
        return serviceRepository.save(service)
    }

    fun deleteService(
        serviceId: Long,
        clientId: String,
    ) {
        val service =
            serviceRepository.findById(serviceId).orElseThrow { NOT_FOUND_EXCEPTION }

        if (service.clientId != clientId) {
            throw PERMISSION_DENIED
        }

        serviceRepository.deleteById(serviceId)
    }

    fun getAll(): List<Service> = serviceRepository.findAll()

    fun getByIds(ids: List<Long>) = serviceRepository.findAllByIdIn(ids)

    fun getAllByClientId(clientId: String) = serviceRepository.findAllByClientId(clientId)

    fun getById(id: Long) = serviceRepository.findById(id).orElseThrow { NOT_FOUND_EXCEPTION }
}
