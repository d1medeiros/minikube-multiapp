package com.example.apiarchetypereactive.controller

import com.example.apiarchetypereactive.component.EventManager
import com.example.apiarchetypereactive.model.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@Disabled
@SpringBootTest
@OptIn(ExperimentalCoroutinesApi::class)
class EventManagerTest {
    @Autowired
    lateinit var eventManager: EventManager

    lateinit var now: LocalDateTime

    @BeforeEach
    internal fun setUp() {
        now = LocalDateTime.of(2022, 11, 6, 0, 0, 0)
    }

    @Test
    @Order(1)
    fun `test 1`() = runTest {
        eventManager.run(now.plusDays(1L))
    }

    @Test
    @Disabled
    @Order(2)
    fun `test 2`() = runTest {
        eventManager.run(now.plusDays(2))
    }

    @Test
    @Order(3)
//    @Disabled
    fun `test 3`() = runTest {
        eventManager.run(now.plusWeeks(1))
    }

}