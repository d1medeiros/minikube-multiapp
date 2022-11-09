package com.example.apiarchetypereactive.config.database

import com.example.apiarchetypereactive.config.properties.DataBaseProperties
import com.github.jasync.r2dbc.mysql.JasyncConnectionFactory
import com.github.jasync.sql.db.mysql.pool.MySQLConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyncConfig(

) {

    @Bean
    fun jasyncConnectionFactory(dataBaseProperties: DataBaseProperties): JasyncConnectionFactory {
        return JasyncConnectionFactory(
            MySQLConnectionFactory(
                com.github.jasync.sql.db.Configuration(
                    username = dataBaseProperties.username!!,
                    host = dataBaseProperties.url!!,
                    port = dataBaseProperties.port!!,
                    password = dataBaseProperties.password!!,
                    database = dataBaseProperties.name!!,
                    maximumMessageSize = dataBaseProperties.maximumMessageSize!!,
                )
            )
        )
    }


}