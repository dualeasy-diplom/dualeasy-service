package gr.project.dualeasy.service

import gr.project.dualeasy.common.ApiException.Companion.NOT_FOUND_EXCEPTION
import gr.project.dualeasy.data.dto.slot.BookedSlotProjection
import gr.project.dualeasy.data.dto.slot.SlotRequestDto
import gr.project.dualeasy.data.model.CalendarSlot
import gr.project.dualeasy.data.model.CalendarSlot.CalendarSlotStatus
import gr.project.dualeasy.data.repository.CalendarSlotRepository
import org.springframework.stereotype.Service

/**
 * Сервис для управления временными слотами (создание, бронирование, подтверждение и т.д.).
 */
@Service
class BookingService(
    private val calendarSlotRepository: CalendarSlotRepository,
) {
    /**
     * Создаёт список слотов на основе входных данных.
     *
     * @param request объект, содержащий список временных интервалов
     * @param ownerId идентификатор владельца слотов
     * @return список созданных слотов
     */
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

    /**
     * Возвращает слот по его ID.
     *
     * @param id идентификатор слота
     * @return найденный слот
     * @throws NOT_FOUND_EXCEPTION если слот не найден
     */
    fun getById(id: Long): CalendarSlot = calendarSlotRepository.findById(id).orElseThrow { throw NOT_FOUND_EXCEPTION }

    /**
     * Удаляет слот по ID.
     *
     * @param id идентификатор удаляемого слота
     */
    fun deleteSlot(id: Long) {
        calendarSlotRepository.deleteById(id)
    }

    /**
     * Подтверждает слот, изменяя его статус на [CalendarSlotStatus.CONFIRMED].
     *
     * @param id идентификатор подтверждаемого слота
     * @return обновлённый слот
     */
    fun confirmSlot(id: Long): CalendarSlot {
        val slot = getById(id)
        return calendarSlotRepository.save(slot.copy(status = CalendarSlotStatus.CONFIRMED))
    }

    /**
     * Завершает слот, изменяя его статус на [CalendarSlotStatus.COMPLETED].
     *
     * @param id идентификатор завершаемого слота
     * @return обновлённый слот
     */
    fun completeSlot(id: Long): CalendarSlot {
        val slot = getById(id)
        return calendarSlotRepository.save(slot.copy(status = CalendarSlotStatus.COMPLETED))
    }

    /**
     * Бронирует слот, устанавливая клиента, услугу, заметку и статус [CalendarSlotStatus.BOOKED].
     *
     * @param id идентификатор слота
     * @param clientId идентификатор клиента
     * @param serviceId идентификатор бронируемой услуги
     * @param note дополнительная информация (необязательно)
     * @return обновлённый слот
     * @throws IllegalStateException если слот недоступен для бронирования
     */
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

    /**
     * Возвращает все доступные слоты для указанного владельца.
     *
     * @param ownerId идентификатор владельца
     * @return список доступных слотов
     */
    fun getSlotsByOwner(ownerId: String): List<CalendarSlot> =
        calendarSlotRepository.findAllByOwnerIdAndStatus(ownerId, CalendarSlotStatus.AVAILABLE)

    /**
     * Возвращает все забронированные и подтверждённые слоты для владельца.
     *
     * @param ownerId идентификатор владельца
     * @return список проекций забронированных слотов
     */
    fun getBookedSlotsForOwner(ownerId: String): List<BookedSlotProjection> = calendarSlotRepository.findBookedSlotsForOwner(ownerId)
}
