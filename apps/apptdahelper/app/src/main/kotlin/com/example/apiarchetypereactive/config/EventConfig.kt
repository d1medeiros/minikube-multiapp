package com.example.apiarchetypereactive.config

import com.example.apiarchetypereactive.component.DailyEventTrigger
import com.example.apiarchetypereactive.component.DefaultEventTrigger
import com.example.apiarchetypereactive.component.DelayedEventTrigger
import com.example.apiarchetypereactive.component.EventTrigger
import com.example.apiarchetypereactive.repository.FullEventRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class EventConfig(
    private val fullEventRepository: FullEventRepository
) {

    @Bean
    fun eventMap(): Sequence<EventTrigger> {
        return arrayListOf(
            DefaultEventTrigger(fullEventRepository),
            DailyEventTrigger(fullEventRepository),
            DelayedEventTrigger(fullEventRepository),
        ).asSequence()
    }
}