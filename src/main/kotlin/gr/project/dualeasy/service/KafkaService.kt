package gr.project.dualeasy.service

import gr.project.dualeasy.data.kafka.SearchRequest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaService(
    private val searchTemplate: KafkaTemplate<String, SearchRequest>,
) {
    fun sendToSearch(message: SearchRequest) {
        searchTemplate.send("dualeasy-search", message)
    }
}
