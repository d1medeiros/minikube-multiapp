package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Event
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface EventRepository: CoroutineCrudRepository<Event, Long> {
}


