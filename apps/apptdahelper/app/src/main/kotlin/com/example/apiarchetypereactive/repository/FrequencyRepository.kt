package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Frequency
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface FrequencyReadRepository : CoroutineCrudRepository<Frequency, Long> {
    suspend fun findByEventId(eventId: Long): Frequency?
}

interface FrequencyRepository : CoroutineCrudRepository<Frequency, Long> {
    suspend fun findByEventId(eventId: Long): Frequency?
}