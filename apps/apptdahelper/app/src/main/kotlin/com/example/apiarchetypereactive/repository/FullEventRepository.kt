package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Event
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class FullEventRepository(
    private val eventRepository: EventWriteRepository,
    private val frequencyRepository: FrequencyRepository
) {
    val Logger: Logger = LoggerFactory.getLogger(javaClass)


    suspend fun count(): Long {
        return eventRepository.count()
    }

    suspend fun findById(id: Long): Event {
        val event = eventRepository.findById(id)
            ?: throw Exception("event not found")
        return findFrequency(event)
    }

    private suspend fun findFrequency(event: Event): Event {
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


    suspend fun save(event: Event): Event {
        Logger.info("salvando {}", event)
        val eventSaved = eventRepository.save(event)
        event.frequency?.let {
            it.eventId = eventSaved.id
            frequencyRepository.save(it)
        }
        return eventSaved
    }

    suspend fun deleteAll() {
        frequencyRepository.deleteAll()
        eventRepository.deleteAll()
    }

    fun saveAll(baseEvents: List<Event>): List<Event> {
        val fullrepo = this
        return runBlocking {
            baseEvents.map {
                async {
                    fullrepo.save(it)
                }
            }.awaitAll()
        }
    }

    suspend fun existsByNotebookIdAndLabel(dailyList: Long, label: String): Boolean {
        return eventRepository.existsByNotebookIdAndLabel(dailyList, label) > 0
    }

    suspend fun findByNotebookIdAndLabel(dailyList: Long, label: String): Event? {
        return eventRepository.findByNotebookIdAndLabel(dailyList, label)?.let {
            return findFrequency(it)
        }
    }

    suspend fun delete(event: Event) {
        Logger.info("deleting {}", event)
        val id = event.id!!
        frequencyRepository.deleteById(id)
        eventRepository.deleteById(id)
    }
}

