package gr.project.dualeasy.service

import gr.project.dualeasy.common.ApiException.Companion.NOT_FOUND_EXCEPTION
import gr.project.dualeasy.data.dto.slot.BookedSlotProjection
import gr.project.dualeasy.data.dto.slot.SlotRequestDto
import gr.project.dualeasy.data.model.CalendarSlot
import gr.project.dualeasy.data.model.CalendarSlot.CalendarSlotStatus
import gr.project.dualeasy.data.repository.CalendarSlotRepository
import org.springframework.stereotype.Service

@Service
class BookingService(
    private val calendarSlotRepository: CalendarSlotRepository,
) {
    fun createSlots(
        request: SlotRequestDto,
        ownerId: String,
    ): List<CalendarSlot> =
        calendarSlotRepository.saveAll(
            request.slots.map {
                CalendarSlot(
                    ownerId = ownerId,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    status = CalendarSlotStatus.AVAILABLE,
                )
            },
        )

    fun getById(id: Long): CalendarSlot = calendarSlotRepository.findById(id).orElseThrow { throw NOT_FOUND_EXCEPTION }

    fun deleteSlot(id: Long) {
        calendarSlotRepository.deleteById(id)
    }

    fun confirmSlot(id: Long): CalendarSlot {
        val slot = getById(id)
        return calendarSlotRepository.save(slot.copy(status = CalendarSlotStatus.CONFIRMED))
    }

    fun completeSlot(id: Long): CalendarSlot {
        val slot = getById(id)
        return calendarSlotRepository.save(slot.copy(status = CalendarSlotStatus.COMPLETED))
    }

    fun bookSlot(
        id: Long,
        clientId: String,
        serviceId: Long,
        note: String?,
    ): CalendarSlot {
        val slot = getById(id)
        if (slot.status != CalendarSlotStatus.AVAILABLE) {
            throw IllegalStateException("Слот не доступен для записи")
        }

        return calendarSlotRepository.save(
            slot.copy(
                clientId = clientId,
                serviceId = serviceId,
                note = note,
                status = CalendarSlotStatus.BOOKED,
            ),
        )
    }

    fun getSlotsByOwner(ownerId: String): List<CalendarSlot> =
        calendarSlotRepository.findAllByOwnerIdAndStatus(ownerId, CalendarSlotStatus.AVAILABLE)

    fun getBookedSlotsForOwner(ownerId: String): List<BookedSlotProjection> {
        return calendarSlotRepository.findBookedSlotsForOwner(ownerId)
    }

}
