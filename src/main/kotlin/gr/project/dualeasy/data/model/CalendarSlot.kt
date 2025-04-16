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
    val clientId: Long,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    @Enumerated(EnumType.STRING)
    val status: CalendarSlotStatus,
    val note: String,
) {
    enum class CalendarSlotStatus {
        UNAVAILABLE,
        AVAILABLE,
        BOOKED,
        CONFIRMED,
        COMPLETED,
    }
}
