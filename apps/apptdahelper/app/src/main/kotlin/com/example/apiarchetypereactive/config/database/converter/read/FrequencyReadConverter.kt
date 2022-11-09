package com.example.apiarchetypereactive.config.database.converter.read

import com.example.apiarchetypereactive.model.Frequency
import com.example.apiarchetypereactive.model.Subject
import com.github.jasync.r2dbc.mysql.JasyncRow
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter

@ReadingConverter
class FrequencyReadConverter : Converter<JasyncRow, Frequency> {
    override fun convert(r: JasyncRow): Frequency? {
        if(r.get("id", Long::class.java) == null)
            return null
        return Frequency(
            id = r.get("id", Long::class.java),
            eventId = r.get("event_id", Long::class.java),
            times = r.get("times", Int::class.java)
                ?: 0,
            subject = r.get("subject", String::class.java)
                ?.let { Subject.valueOf(it) } ?: Subject.NONE,
        )
    }
}