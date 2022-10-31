package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.config.Logger
import com.example.apiarchetypereactive.extension.plus
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.repository.FullEventRepository
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime

class TimeControl(
    private val eventTrigger: EventTrigger
) {

    fun run(dateTime: LocalDateTime, event: Event) = runBlocking {
        Logger.info("time: {} event: {}", dateTime, event.label)
        val frequency = event.frequency ?: throw Exception("frequency is null")
        val isAfter = dateTime.isAfter(dateTime.plus(frequency))
        if (isAfter) eventTrigger.send(event)
    }

}

class EventManager(
    private val fullEventRepository: FullEventRepository,
    private val timeControl: TimeControl
) {

    private var list = runBlocking {
        fullEventRepository.findAll()
    }


    fun run(now: LocalDateTime) {
        for (event in list) {
            timeControl.run(now, event)
        }
    }
}