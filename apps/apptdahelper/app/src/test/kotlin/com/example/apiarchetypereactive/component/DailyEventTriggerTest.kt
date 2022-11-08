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
internal class DailyEventTriggerTest {

    @MockK
    lateinit var fullEventRepository: FullEventRepository

    @InjectMockKs
    lateinit var dailyEventTrigger: DailyEventTrigger

    @MockK
    lateinit var frequency: Frequency
    @MockK
    lateinit var event: Event

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
    fun `send, is limit and lastEvent not null`() = runTest{
        //setting limit
        dataBase = now.minusWeeks(1L)
        event = spyk(
            Event(
                id = 1L,
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        dailyEvent = mockk()
        every { frequency.times }.returns(1)
        every { frequency.subject }.returns(Subject.DAY)
        coJustRun { fullEventRepository.delete(any()) }
        val result = dailyEventTrigger.send(event, dailyEvent, now)
        coVerify { fullEventRepository.delete(any()) }
        assertEquals(dailyEvent, result)
    }

    @Test
    fun `send, is NOT limit and is daily and has NOT lastEvent`() = runTest{
        //setting daily and not limit
        dataBase = now.minusDays(1L)
        event = spyk(
            Event(
                id = 1L,
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        val newSavedEvent: Event = mockk()
        frequency = spyk(Frequency(times = 1, subject = Subject.DAY))
        every { event.frequency }.returns(frequency)
        coEvery { fullEventRepository.save(any()) }.returns(newSavedEvent)
        val result = dailyEventTrigger.send(event, dailyEvent, now)
        assertEquals(newSavedEvent, result)
    }

    @Test
    fun `send, is NOT limit and is NOT daily and has lastEvent`() = runTest{
        dataBase = now.plusDays(1L)
        event = spyk(
            Event(
                id = 1L,
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        dailyEvent = mockk()
        every { dailyEvent!!.id }.returns(2L)
        val result = dailyEventTrigger.send(event, dailyEvent, now)
        coVerify(exactly = 0) { fullEventRepository.save(any()) }
        coVerify(exactly = 0) { fullEventRepository.delete(any()) }
        assertEquals(dailyEvent, result)
    }

    @Test
    fun `send, is NOT limit and is NOT daily and has NOT lastEvent`() = runTest{
        dataBase = now.plusDays(1L)
        event = spyk(
            Event(
                id = 1L,
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        val result = dailyEventTrigger.send(event, dailyEvent, now)
        coVerify(exactly = 0) { fullEventRepository.save(any()) }
        coVerify(exactly = 0) { fullEventRepository.delete(any()) }
        assertNull(result)
    }

}