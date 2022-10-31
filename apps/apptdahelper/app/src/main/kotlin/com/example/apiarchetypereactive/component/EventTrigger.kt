package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.config.Logger
import com.example.apiarchetypereactive.model.Event

interface EventTrigger {
    fun send(event: Event)
}

class EventDefault : EventTrigger {
    override fun send(event: Event) {
        Logger.info("sending event {} {}", event.id, event.label)
    }

}