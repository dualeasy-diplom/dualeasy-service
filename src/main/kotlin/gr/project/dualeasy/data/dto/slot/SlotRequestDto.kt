package gr.project.dualeasy.data.dto.slot

import java.time.LocalDateTime

data class SlotRequestDto(
    val slots: List<Slot>,
) {
    data class Slot(
        val startTime: LocalDateTime,
        val endTime: LocalDateTime,
    )
}
