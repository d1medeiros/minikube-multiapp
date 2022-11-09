package com.example.apiarchetypereactive.config

import com.example.apiarchetypereactive.config.database.converter.read.EventReadConverter
import com.example.apiarchetypereactive.config.database.converter.read.FrequencyReadConverter
import com.example.apiarchetypereactive.config.database.converter.writing.EventWritingConverter
import com.example.apiarchetypereactive.config.database.converter.writing.FrequencyWritingConverter
import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Configuration
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration


@Configuration
class R2dbcConfig(
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

