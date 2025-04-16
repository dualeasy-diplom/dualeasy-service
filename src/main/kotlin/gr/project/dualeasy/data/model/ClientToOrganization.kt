package gr.project.dualeasy.data.model

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "client_to_organization")
data class ClientToOrganization(
    @EmbeddedId
    val id: ClientToOrganizationId,
    @Enumerated(EnumType.STRING)
    val role: Role,
) {
    enum class Role {
        BASE,
        ADMIN,
    }

    @Embeddable
    data class ClientToOrganizationId(
        val clientId: String,
        val organizationId: Long,
    )
}
