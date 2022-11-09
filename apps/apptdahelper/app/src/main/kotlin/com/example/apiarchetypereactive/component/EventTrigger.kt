package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.model.Event
import java.time.LocalDateTime

interface EventTrigger {
    suspend fun send(
        event: Event,
        lastEvent: Event?,
        now: LocalDateTime,
    ): Event?

}

const val defaultList = 1L
const val dailyList = 2L
const val delayedList = 3L

