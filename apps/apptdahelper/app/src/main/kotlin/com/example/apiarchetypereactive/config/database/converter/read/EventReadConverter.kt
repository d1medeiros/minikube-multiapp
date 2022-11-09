package com.example.apiarchetypereactive.config.database.converter.read

import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.model.Type
import com.github.jasync.r2dbc.mysql.JasyncRow
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import java.time.LocalDateTime

@ReadingConverter
class EventReadConverter : Converter<JasyncRow, Event> {
    override fun convert(r: JasyncRow): Event {
        return Event(
            id = r.get("id", Long::class.java)
                ?: throw Exception("id null"),
            label = r.get("label", String::class.java)
                ?: "",
            dataBase = r.get("data_base", LocalDateTime::class.java)
                ?: throw Exception("data_base null"),
            active = r.get("active", Byte::class.java)
                ?: throw Exception("active null"),
            type = r.get("type", String::class.java)
                ?.let { Type.valueOf(it) } ?: Type.NONE,
            notebookId = r.get("notebook_id", Long::class.java)!!,
        )
    }
}