package com.example.apiarchetypereactive.config.database.converter.writing

import com.example.apiarchetypereactive.model.Frequency
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.mapping.OutboundRow
import org.springframework.r2dbc.core.Parameter

@WritingConverter
class FrequencyWritingConverter : Converter<Frequency, OutboundRow> {
    override fun convert(e: Frequency) =
        OutboundRow(
            mapOf(
                "id" to Parameter.fromOrEmpty(e.id, Long::class.java),
                "event_id" to Parameter.fromOrEmpty(e.eventId, Long::class.java),
                "times" to Parameter.fromOrEmpty(e.times, Int::class.java),
                "subject" to Parameter.fromOrEmpty(e.subject.name, String::class.java),
            )
        )

}