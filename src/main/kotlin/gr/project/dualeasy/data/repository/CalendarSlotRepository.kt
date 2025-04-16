package gr.project.dualeasy.data.repository

import gr.project.dualeasy.data.model.CalendarSlot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CalendarSlotRepository : JpaRepository<CalendarSlot, Long> {
    fun findAllByClientId(clientId: Long): List<CalendarSlot>
}
