package gr.project.dualeasy.data.repository

import gr.project.dualeasy.data.model.UserProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserProfileRepository : JpaRepository<UserProfile, String>
