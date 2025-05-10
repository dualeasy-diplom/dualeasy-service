package gr.project.dualeasy.data.repository

import gr.project.dualeasy.data.dto.slot.BookedSlotProjection
import gr.project.dualeasy.data.model.CalendarSlot
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CalendarSlotRepository : JpaRepository<CalendarSlot, Long> {
    fun findAllByOwnerIdAndStatus(
        ownerId: String,
        status: CalendarSlot.CalendarSlotStatus,
    ): List<CalendarSlot>

    @Query(
        """
    SELECT 
        cs.id AS slotId,
        cs.serviceId AS serviceId,
        cs.startTime AS startTime,
        cs.endTime AS endTime,
        cs.status AS status,
        cs.note AS note,
        s.name AS serviceName,
        s.mainPhoto AS servicePhoto,
        s.price AS servicePrice,
        s.address AS serviceAddress
    FROM CalendarSlot cs
    JOIN Service s ON cs.serviceId = s.id
    WHERE cs.ownerId = :ownerId 
      AND cs.status IN (gr.project.dualeasy.data.model.CalendarSlot.CalendarSlotStatus.BOOKED, gr.project.dualeasy.data.model.CalendarSlot.CalendarSlotStatus.CONFIRMED)
    """,
    )
    fun findBookedSlotsForOwner(
        @Param("ownerId") ownerId: String,
    ): List<BookedSlotProjection>
}
