package gr.project.dualeasy.data.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user_profile")
data class UserProfile(
    @Id
    val clientId: String,
    val photoUrl: String?,
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val phoneNumber: String?,
    val about: String?,
)
