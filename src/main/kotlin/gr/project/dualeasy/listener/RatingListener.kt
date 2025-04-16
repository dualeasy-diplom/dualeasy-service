package gr.project.dualeasy.listener

import gr.project.dualeasy.data.dto.RatingUpdateEvent
import gr.project.dualeasy.service.ServiceService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class RatingListener(
    private val serviceService: ServiceService,
) {
    @KafkaListener(
        topics = ["dualeasy-rating"],
        groupId = "\${spring.kafka.consumer.group-id}",
    )
    fun pooling(message: RatingUpdateEvent) {
        serviceService.updateService(message.serviceId, message.averageScore)
        println("✅ Received from Kafka: $message")
    }
}

