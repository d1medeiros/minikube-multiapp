package com.example.integrationtest

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@EnableAutoConfiguration
@ExtendWith(SpringExtension::class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
abstract class IntegrationTest {
    companion object {
        @Container
        private val mysqlContainer = MySQLContainer<Nothing>("mysql:latest").apply {
            withDatabaseName("db")
            withUsername("sa")
            withPassword("sa")
            withCommand("--default-authentication-plugin=mysql_native_password")
            withInitScript("db/data.sql")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("db.r2dbc.url", mysqlContainer::getHost)
            registry.add("db.r2dbc.port", mysqlContainer::getFirstMappedPort)
            registry.add("db.r2dbc.name", mysqlContainer::getDatabaseName)
            registry.add("db.r2dbc.password", mysqlContainer::getPassword)
            registry.add("db.r2dbc.username", mysqlContainer::getUsername)
        }
    }
}