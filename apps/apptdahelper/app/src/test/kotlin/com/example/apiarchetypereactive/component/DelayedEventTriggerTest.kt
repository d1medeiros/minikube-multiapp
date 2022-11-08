package com.example.apiarchetypereactive.component

import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.model.Frequency
import com.example.apiarchetypereactive.model.Subject
import com.example.apiarchetypereactive.model.Type
import com.example.apiarchetypereactive.repository.FullEventRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDateTime


@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
internal class DelayedEventTriggerTest {
    @MockK
    lateinit var fullEventRepository: FullEventRepository

    @InjectMockKs
    lateinit var delayedEventTrigger: DelayedEventTrigger

    @MockK
    lateinit var event: Event
    @MockK
    lateinit var frequency: Frequency

    val label = "label"
    var dailyEvent: Event? = null
    lateinit var now: LocalDateTime
    lateinit var dataBase: LocalDateTime

    @BeforeEach
    internal fun setUp() {
        now = LocalDateTime.now()
        dataBase = now
    }

    @Test
    fun `send, has daily and is limit`() = runTest{
        dataBase = now.minusWeeks(1L)
        event = spyk(
            Event(
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        dailyEvent = mockk()
        val newSavedEvent: Event = mockk()
        frequency = spyk(Frequency(times = 1, subject = Subject.DAY))
        every { event.id }.returns(1L)
        every { event.frequency }.returns(frequency)
        coEvery { fullEventRepository.save(any()) }.returns(newSavedEvent)
        val result = delayedEventTrigger.send(event, dailyEvent, now)
        assertEquals(newSavedEvent, result)
    }

    @Test
    fun `send, is not limit`() = runTest{
        dataBase = now.minusDays(1L)
        event = spyk(
            Event(
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        dailyEvent = mockk()
        frequency = spyk(Frequency(times = 1, subject = Subject.DAY))
        every { event.id }.returns(1L)
        every { event.frequency }.returns(frequency)
        val result = delayedEventTrigger.send(event, dailyEvent, now)
        coVerify(exactly = 0) { fullEventRepository.save(any()) }
        assertNull(result)
    }

    @Test
    fun `send, has NO daily and is limit`() = runTest{
        dataBase = now.minusDays(1L)
        event = spyk(
            Event(
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        frequency = spyk(Frequency(times = 1, subject = Subject.DAY))
        every { event.id }.returns(1L)
        every { event.frequency }.returns(frequency)
        val result = delayedEventTrigger.send(event, dailyEvent, now)
        coVerify(exactly = 0) { fullEventRepository.save(any()) }
        assertNull(result)
    }
}