package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Event
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class REventRepository(
    private val eventRepository: EventReadRepository,
    private val frequencyRepository: FrequencyReadRepository
) {
    val Logger: Logger = LoggerFactory.getLogger(javaClass)

    suspend fun count(): Long {
        return eventRepository.count()
    }

    suspend fun findById(id: Long): Event {
        val event = eventRepository.findById(id)
            ?: throw Exception("event not found")
        val frequency = frequencyRepository.findByEventId(event.id!!)
            ?: throw Exception("frequency not found")
        return event.apply {
            this.frequency = frequency
        }
    }

    suspend fun findAll(id: Long): List<Event> {
        Logger.info("searching eventos")
        return eventRepository.findAllByNotebookId(id)
            .map {
                Logger.info("searching frequency")
                it.frequency = frequencyRepository.findByEventId(it.id!!)
                it
            }.toList()
    }

}