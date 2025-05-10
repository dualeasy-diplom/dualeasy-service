package gr.project.dualeasy.controller

import gr.project.dualeasy.common.model.RequestContainer
import gr.project.dualeasy.data.dto.slot.BookSlotRequest
import gr.project.dualeasy.data.dto.slot.BookedSlotProjection
import gr.project.dualeasy.data.dto.slot.SlotRequestDto
import gr.project.dualeasy.data.model.CalendarSlot
import gr.project.dualeasy.service.BookingService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/slots")
class SlotController(
    private val bookingService: BookingService,
) {
    @PostMapping
    fun createSlots(request: RequestContainer<SlotRequestDto>) {
        bookingService.createSlots(request.request, request.clientId!!)
    }

    @DeleteMapping("/{id}")
    fun deleteSlot(
        @PathVariable id: Long,
    ) = bookingService.deleteSlot(id)

    @PatchMapping("/{id}/confirm")
    fun confirmSlot(
        @PathVariable id: Long,
    ): CalendarSlot = bookingService.confirmSlot(id)

    @PatchMapping("/{id}/complete")
    fun completeSlot(
        @PathVariable id: Long,
    ): CalendarSlot = bookingService.completeSlot(id)

    @PostMapping("/{id}/book")
    fun bookSlot(
        @RequestHeader("X-Client-Id") clientId: String,
        @PathVariable id: Long,
        @RequestBody request: BookSlotRequest,
    ): CalendarSlot = bookingService.bookSlot(id, clientId, request.serviceId, request.note)

    @GetMapping("/owner/{ownerId}")
    fun getSlotsByOwner(
        @PathVariable ownerId: String,
    ): List<CalendarSlot> = bookingService.getSlotsByOwner(ownerId)

    @GetMapping("/owner/booked")
    fun getBookedSlotsForOwner(
        @RequestHeader("X-Client-Id") clientId: String,
    ): List<BookedSlotProjection> = bookingService.getBookedSlotsForOwner(clientId)
}
