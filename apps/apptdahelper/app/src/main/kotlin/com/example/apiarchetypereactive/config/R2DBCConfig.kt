package com.example.apiarchetypereactive.config

//import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory
//import com.github.jasync.r2dbc.mysql.JasyncRow
//import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.model.Frequency
import com.example.apiarchetypereactive.model.Subject
import com.example.apiarchetypereactive.model.Type
import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory
import com.github.jasync.r2dbc.mysql.JasyncRow
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.mapping.OutboundRow
import org.springframework.r2dbc.core.Parameter
import java.time.LocalDateTime


@Configuration
class MyAppConfig(
    val jasyncConnectionFactory: JasyncConnectionFactory
) : AbstractR2dbcConfiguration() {

    override fun connectionFactory(): ConnectionFactory {
        return jasyncConnectionFactory
    }

    override fun getCustomConverters(): List<Any> {
        return listOf(
            EventReadConverter(),
            EventWritingConverter(),
            FrequencyReadConverter(),
            FrequencyWritingConverter()
        )
    }
}

@Configuration
class JasyncConfig {

    @Bean
    fun jasyncConnectionFactory(): JasyncConnectionFactory {
        return JasyncConnectionFactory(
            MySQLConnectionFactory(
                com.github.jasync.sql.db.Configuration(
                    username = "sa",
                    host = "localhost",
                    port = 3306,
                    password = "sa",
                    database = "db",
                    maximumMessageSize = 100_000,
                )
            )
        )
    }


}

fun Byte.toBoolean(): Boolean {
    return when (this) {
        0.toByte() -> true
        else -> false
    }
}
fun Boolean.toByte(): Byte {
    return when (this) {
        true -> 0.toByte()
        else -> 1.toByte()
    }
}

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