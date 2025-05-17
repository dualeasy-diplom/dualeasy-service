package gr.project.dualeasy.controller

import gr.project.dualeasy.common.model.RequestContainer
import gr.project.dualeasy.data.dto.slot.BookSlotRequest
import gr.project.dualeasy.data.dto.slot.BookedSlotProjection
import gr.project.dualeasy.data.dto.slot.SlotRequestDto
import gr.project.dualeasy.data.model.CalendarSlot
import gr.project.dualeasy.service.BookingService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/slots")
@Tag(name = "Slots", description = "Управление временными слотами и бронированием")
class SlotController(
    private val bookingService: BookingService,
) {
    @Operation(summary = "Создать слоты")
    @PostMapping
    fun createSlots(request: RequestContainer<SlotRequestDto>) {
        bookingService.createSlots(request.request, request.clientId!!)
    }

    @Operation(summary = "Удалить слот")
    @DeleteMapping("/{id}")
    fun deleteSlot(
        @PathVariable id: Long,
    ) = bookingService.deleteSlot(id)

    @Operation(summary = "Подтвердить слот")
    @PatchMapping("/{id}/confirm")
    fun confirmSlot(
        @PathVariable id: Long,
    ): CalendarSlot = bookingService.confirmSlot(id)

    @Operation(summary = "Завершить слот")
    @PatchMapping("/{id}/complete")
    fun completeSlot(
        @PathVariable id: Long,
    ): CalendarSlot = bookingService.completeSlot(id)

    @Operation(summary = "Забронировать слот")
    @PostMapping("/{id}/book")
    fun bookSlot(
        @PathVariable id: Long,
        requestContainer: RequestContainer<BookSlotRequest>,
    ): CalendarSlot =
        bookingService.bookSlot(
            id,
            requestContainer.clientId!!,
            requestContainer.request.serviceId,
            requestContainer.request.note,
        )

    @Operation(summary = "Получить слоты по владельцу")
    @GetMapping("/owner/{ownerId}")
    fun getSlotsByOwner(
        @PathVariable ownerId: String,
    ): List<CalendarSlot> = bookingService.getSlotsByOwner(ownerId)

    @Operation(summary = "Получить забронированные слоты для владельца")
    @GetMapping("/owner/booked")
    fun getBookedSlotsForOwner(
        @RequestHeader("X-Client-Id") clientId: String,
    ): List<BookedSlotProjection> = bookingService.getBookedSlotsForOwner(clientId)
}
