package gr.project.dualeasy.data.repository

import gr.project.dualeasy.data.model.ClientToOrganization
import gr.project.dualeasy.data.model.ClientToOrganization.ClientToOrganizationId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientToOrganizationRepository : JpaRepository<ClientToOrganization, ClientToOrganizationId> {
    fun findAllByIdClientId(clientId: String): List<ClientToOrganization>
    fun findAllByIdOrganizationId(organizationId: Long): List<ClientToOrganization>
}
