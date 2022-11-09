package com.example.integrationtest

import com.example.apiarchetypereactive.component.EventManager
import com.example.apiarchetypereactive.model.*
import com.example.apiarchetypereactive.repository.EventReadRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime


@Testcontainers
@EnableAutoConfiguration
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [TestConfig::class])
@OptIn(ExperimentalCoroutinesApi::class)
class EventManagerTest {

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

    @Autowired
    lateinit var eventManager: EventManager
    @Autowired
    lateinit var eventReadRepository: EventReadRepository

    lateinit var now: LocalDateTime

    @BeforeEach
    internal fun setUp() {
        now = LocalDateTime.of(2022, 11, 6, 0, 0, 0)
    }

    @Test
    @Order(1)
    fun `test 1`() = runTest {
        eventManager.run(now.plusDays(1L))
        val all = eventReadRepository.findAll().toList()
        all.first { it.label == "Dar comida" && it.notebookId == 1L }
            .run {
                assertEquals(7, this.dataBase.dayOfMonth)
            }
        all.first { it.label == "Dar comida" && it.notebookId == 2L }
            .run {
                assertEquals(6, this.dataBase.dayOfMonth)
            }
    }

    @Test
    @Order(2)
    fun `test 2`() = runTest {
        eventManager.run(now.plusDays(2))
        val all = eventReadRepository.findAll().toList()
        all.first { it.label == "Dar comida" && it.notebookId == 1L }
            .run {
                assertEquals(7, this.dataBase.dayOfMonth)
            }
        all.first { it.label == "Dar comida" && it.notebookId == 2L }
            .run {
                assertEquals(6, this.dataBase.dayOfMonth)
            }
    }

    @Test
    @Order(3)
    fun `test 3`() = runTest {
            eventManager.run(now.plusWeeks(2))
            val all = eventReadRepository.findAll().toList()
            all.first { it.label == "Dar comida" && it.notebookId == 1L }
                .run {
                    assertEquals(7, this.dataBase.dayOfMonth)
                }
            assertNull(all.firstOrNull {
                it.label == "Dar comida" && it.notebookId == 2L
            })
            all.first { it.label == "Dar comida" && it.notebookId == 3L }
                .run {
                    assertEquals(6, this.dataBase.dayOfMonth)
                }
    }

}