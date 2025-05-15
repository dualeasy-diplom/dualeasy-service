package gr.project.dualeasy

import gr.project.dualeasy.common.ApiException
import gr.project.dualeasy.data.kafka.toSearchRequest
import gr.project.dualeasy.data.model.Service
import gr.project.dualeasy.data.repository.ServiceRepository
import gr.project.dualeasy.service.KafkaService
import gr.project.dualeasy.service.ServiceService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import java.math.BigDecimal
import java.util.*

class ServiceServiceTest {

    private val repository = mock(ServiceRepository::class.java)
    private val kafkaService = mock(KafkaService::class.java)
    private val serviceService = ServiceService(repository, kafkaService)

    @Test
    fun `createService should save and send to kafka`() {
        val service = Service(id = 1L, clientId = "client", name = "Test", price = BigDecimal.TEN)
        `when`(repository.save(service)).thenReturn(service)

        val result = serviceService.createService(service)

        verify(repository).save(service)
        verify(kafkaService).sendToSearch(service.toSearchRequest())
        assertEquals(service, result)
    }

    @Test
    fun `updateService should update existing service`() {
        val original = Service(id = 1L, clientId = "client", name = "Old", price = BigDecimal.ONE)
        val updated = original.copy(name = "New", price = BigDecimal.TEN)

        `when`(repository.findById(1L)).thenReturn(Optional.of(original))
        `when`(repository.save(any(Service::class.java))).thenReturn(updated)

        val result = serviceService.updateService(updated, 1L)

        verify(repository).save(any(Service::class.java))
        assertEquals("New", result.name)
        assertEquals(BigDecimal.TEN, result.price)
    }

    @Test
    fun `updateService should throw PERMISSION_DENIED`() {
        val original = Service(id = 1L, clientId = "client1", name = "Old", price = BigDecimal.ONE)
        val updated = original.copy(clientId = "client2")

        `when`(repository.findById(1L)).thenReturn(Optional.of(original))

        val ex = assertThrows<ApiException> {
            serviceService.updateService(updated, 1L)
        }
        assertEquals(ApiException.PERMISSION_DENIED.message, ex.message)
    }

    @Test
    fun `deleteService should remove service if client matches`() {
        val service = Service(id = 1L, clientId = "client1")

        `when`(repository.findById(1L)).thenReturn(Optional.of(service))

        serviceService.deleteService(1L, "client1")

        verify(repository).deleteById(1L)
    }

    @Test
    fun `deleteService should throw PERMISSION_DENIED if client does not match`() {
        val service = Service(id = 1L, clientId = "client1")

        `when`(repository.findById(1L)).thenReturn(Optional.of(service))

        val ex = assertThrows<ApiException> {
            serviceService.deleteService(1L, "client2")
        }
        assertEquals(ApiException.PERMISSION_DENIED.message, ex.message)
    }

    @Test
    fun `getById should return service if exists`() {
        val service = Service(id = 1L, clientId = "client1")
        `when`(repository.findById(1L)).thenReturn(Optional.of(service))

        val result = serviceService.getById(1L)

        assertEquals(service, result)
    }

    @Test
    fun `getById should throw NOT_FOUND_EXCEPTION if not found`() {
        `when`(repository.findById(1L)).thenReturn(Optional.empty())

        val ex = assertThrows<ApiException> {
            serviceService.getById(1L)
        }

        assertEquals(ApiException.NOT_FOUND_EXCEPTION.message, ex.message)
    }
}
