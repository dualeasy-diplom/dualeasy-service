package gr.project.dualeasy.data.dto.slot

import java.math.BigDecimal
import java.time.LocalDateTime

interface BookedSlotProjection {
    fun getSlotId(): Long

    fun getStartTime(): LocalDateTime

    fun getEndTime(): LocalDateTime

    fun getStatus(): String

    fun getNote(): String?

    fun getServiceName(): String

    fun getServicePhoto(): String?

    fun getServicePrice(): BigDecimal

    fun getServiceAddress(): String?

    fun getServiceId(): Long
}
