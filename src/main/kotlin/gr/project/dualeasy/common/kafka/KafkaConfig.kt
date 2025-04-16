package gr.project.dualeasy.config.kafka

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaProducerConfig {
    @Bean
    fun createTopic(): NewTopic = NewTopic("dualeasy-search", 1, 1.toShort())
}
