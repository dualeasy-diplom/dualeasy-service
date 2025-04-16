package gr.project.dualeasy.data.repository

import gr.project.dualeasy.data.model.Organization
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository : JpaRepository<Organization, Long> {
    fun findAllByCreatorId(creatorId: String): List<Organization>
}
