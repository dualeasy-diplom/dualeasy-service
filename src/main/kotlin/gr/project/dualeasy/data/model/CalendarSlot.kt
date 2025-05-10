package gr.project.dualeasy.data.model

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "calendar_slot")
data class CalendarSlot(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val ownerId: String,
    val serviceId: Long? = null,
    val clientId: String? = null,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    @Enumerated(EnumType.STRING)
    val status: CalendarSlotStatus = CalendarSlotStatus.AVAILABLE,
    val note: String? = null,
) {
    enum class CalendarSlotStatus {
        UNAVAILABLE,
        AVAILABLE,
        BOOKED,
        CONFIRMED,
        COMPLETED,
    }
}
