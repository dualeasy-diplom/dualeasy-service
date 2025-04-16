package gr.project.dualeasy.data.repository

import gr.project.dualeasy.data.model.Service
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : JpaRepository<Service, Long> {
    fun findAllByClientId(clientId: String): List<Service>

    fun findAllByIdIn(ids: List<Long>): List<Service>
}
