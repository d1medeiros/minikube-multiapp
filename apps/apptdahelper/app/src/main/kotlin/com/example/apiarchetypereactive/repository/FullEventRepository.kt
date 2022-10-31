package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.config.Logger
import com.example.apiarchetypereactive.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class FullEventRepository(
    private val eventRepository: EventRepository,
    private val frequencyRepository: FrequencyRepository
) {

    @Transactional
    suspend fun save(event: Event): Event {
        val frequency = event.frequency
            ?: throw Exception("frequency null")
        val eventSaved = eventRepository.save(event)
        frequency.eventId = eventSaved.id
        frequencyRepository.save(frequency)
        return eventSaved
    }

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

    suspend fun findAll(): List<Event> {
        Logger.info("searching eventos")
        return eventRepository.findAll()
            .map {
                Logger.info("searching frequency")
                it.frequency = frequencyRepository.findByEventId(it.id!!)
                it
            }.toList()
    }

    suspend fun deleteAll() {
        frequencyRepository.deleteAll()
        eventRepository.deleteAll()
    }

    @Transactional
    suspend fun saveAll(baseEvents: Flow<Event>) {
        eventRepository.saveAll(baseEvents)
            .collect {
                val frequency = it.frequency
                    ?: throw Exception("frequency null")
                frequency.eventId = it.id
                val f = frequencyRepository.save(frequency)
                println("$it $f")
            }
    }
}