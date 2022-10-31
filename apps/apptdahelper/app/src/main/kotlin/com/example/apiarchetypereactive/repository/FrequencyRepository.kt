package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Frequency
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface FrequencyRepository : CoroutineCrudRepository<Frequency, Long> {
    suspend fun findByEventId(eventId: Long): Frequency?
}