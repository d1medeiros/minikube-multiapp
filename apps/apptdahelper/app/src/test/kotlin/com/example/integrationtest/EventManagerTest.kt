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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime


@SpringBootTest(classes = [TestConfig::class])
@OptIn(ExperimentalCoroutinesApi::class)
class EventManagerTest: IntegrationTest() {

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