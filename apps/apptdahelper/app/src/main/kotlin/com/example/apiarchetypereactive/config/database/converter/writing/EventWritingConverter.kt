package com.example.apiarchetypereactive.config.database.converter.writing

import com.example.apiarchetypereactive.extension.toByte
import com.example.apiarchetypereactive.model.Event
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.mapping.OutboundRow
import org.springframework.r2dbc.core.Parameter
import java.time.LocalDateTime

@WritingConverter
class EventWritingConverter : Converter<Event, OutboundRow> {

    override fun convert(e: Event) =
        OutboundRow(
            mapOf(
                "id" to Parameter.fromOrEmpty(e.id, Long::class.java),
                "notebook_id" to Parameter.fromOrEmpty(e.notebookId, Long::class.java),
                "label" to Parameter.fromOrEmpty(e.label, String::class.java),
                "data_base" to Parameter.fromOrEmpty(e.dataBase, LocalDateTime::class.java),
                "active" to Parameter.fromOrEmpty(e.active.toByte(), Byte::class.java),
                "type" to Parameter.fromOrEmpty(e.type.name, String::class.java),
            )
        )
}