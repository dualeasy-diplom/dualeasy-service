package gr.project.dualeasy

import gr.project.dualeasy.common.ApiException
import gr.project.dualeasy.data.dto.slot.BookedSlotProjection
import gr.project.dualeasy.data.dto.slot.SlotRequestDto
import gr.project.dualeasy.data.model.CalendarSlot
import gr.project.dualeasy.data.model.CalendarSlot.CalendarSlotStatus.*
import gr.project.dualeasy.data.repository.CalendarSlotRepository
import gr.project.dualeasy.service.BookingService
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import java.time.LocalDateTime
import java.util.*

class BookingServiceTest {

    private val repository = mock(CalendarSlotRepository::class.java)
    private val bookingService = BookingService(repository)

    private val now = LocalDateTime.now()
    private val slot = CalendarSlot(
        id = 1L,
        ownerId = "owner1",
        startTime = now,
        endTime = now.plusHours(1),
        status = AVAILABLE
    )

    @Test
    fun `createSlots should save all slots`() {
        val slotRequest = SlotRequestDto(
            slots = listOf(
                SlotRequestDto.Slot(
                    startTime = now,
                    endTime = now.plusHours(1)
                )
            )
        )
        val expected = listOf(slot)
        `when`(repository.saveAll(anyList())).thenReturn(expected)

        val result = bookingService.createSlots(slotRequest, "owner1")

        verify(repository).saveAll(anyList())
        assertEquals(1, result.size)
    }

    @Test
    fun `getById should return slot if found`() {
        `when`(repository.findById(1L)).thenReturn(Optional.of(slot))

        val result = bookingService.getById(1L)

        assertEquals(slot, result)
    }

    @Test
    fun `getById should throw if not found`() {
        `when`(repository.findById(1L)).thenReturn(Optional.empty())

        val exception = assertThrows<ApiException> {
            bookingService.getById(1L)
        }

        assertEquals(ApiException.NOT_FOUND_EXCEPTION.message, exception.message)
    }

    @Test
    fun `deleteSlot should call repository`() {
        doNothing().`when`(repository).deleteById(1L)

        bookingService.deleteSlot(1L)

        verify(repository).deleteById(1L)
    }

    @Test
    fun `confirmSlot should change status to CONFIRMED`() {
        `when`(repository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(repository.save(any())).thenAnswer { it.arguments[0] }

        val result = bookingService.confirmSlot(1L)

        assertEquals(CONFIRMED, result.status)
    }

    @Test
    fun `completeSlot should change status to COMPLETED`() {
        `when`(repository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(repository.save(any())).thenAnswer { it.arguments[0] }

        val result = bookingService.completeSlot(1L)

        assertEquals(COMPLETED, result.status)
    }

    @Test
    fun `bookSlot should set client and change status`() {
        `when`(repository.findById(1L)).thenReturn(Optional.of(slot))
        `when`(repository.save(any())).thenAnswer { it.arguments[0] }

        val result = bookingService.bookSlot(1L, "client1", 123L, "note")

        assertEquals("client1", result.clientId)
        assertEquals(123L, result.serviceId)
        assertEquals("note", result.note)
        assertEquals(BOOKED, result.status)
    }

    @Test
    fun `bookSlot should throw if slot not available`() {
        val notAvailableSlot = slot.copy(status = BOOKED)
        `when`(repository.findById(1L)).thenReturn(Optional.of(notAvailableSlot))

        val ex = assertThrows<IllegalStateException> {
            bookingService.bookSlot(1L, "client1", 123L, "note")
        }

        assertEquals("Слот не доступен для записи", ex.message)
    }

    @Test
    fun `getSlotsByOwner should return available slots`() {
        val slots = listOf(slot)
        `when`(repository.findAllByOwnerIdAndStatus("owner1", AVAILABLE)).thenReturn(slots)

        val result = bookingService.getSlotsByOwner("owner1")

        assertEquals(1, result.size)
        assertEquals(slot, result[0])
    }

    @Test
    fun `getBookedSlotsForOwner should call repository`() {
        val projections = mock(List::class.java) as List<BookedSlotProjection>
        `when`(repository.findBookedSlotsForOwner("owner1")).thenReturn(projections)

        val result = bookingService.getBookedSlotsForOwner("owner1")

        verify(repository).findBookedSlotsForOwner("owner1")
        assertEquals(projections, result)
    }
}
