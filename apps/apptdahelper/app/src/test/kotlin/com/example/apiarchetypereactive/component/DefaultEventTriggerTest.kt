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
internal class DefaultEventTriggerTest{
    @MockK
    lateinit var fullEventRepository: FullEventRepository

    @InjectMockKs
    lateinit var defaultEventTrigger: DefaultEventTrigger

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
    fun `send, is daily and it has NOT dailyEvent`() = runTest{
        dataBase = now.minusDays(1L)
        event = spyk(
            Event(
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        every { event.id }.returns(1L)
        every { frequency.times }.returns(1)
        every { frequency.subject }.returns(Subject.DAY)
        coEvery {
            fullEventRepository.findByNotebookIdAndLabel(dailyList, label)
        }.returns(null)
        coEvery { fullEventRepository.save(any()) }.returns(event)
        val result = defaultEventTrigger.send(event, dailyEvent, now)
        assertEquals(dailyEvent, result)
    }

    @Test
    fun `send, is NOT daily and it has dailyEvent`() = runTest{
        dataBase = now.plusDays(1L)
        dailyEvent = mockk()
        event = spyk(
            Event(
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        every { event.id }.returns(1L)
        every { frequency.times }.returns(1)
        every { frequency.subject }.returns(Subject.DAY)
        coEvery {
            fullEventRepository.findByNotebookIdAndLabel(dailyList, label)
        }.returns(dailyEvent)
        val result = defaultEventTrigger.send(event, null, now)
        coVerify(exactly = 0) {
            fullEventRepository.save(any())
        }
        assertEquals(dailyEvent, result)
    }

    @Test
    fun `send, is daily and it has dailyEvent`() = runTest{
        dataBase = now.minusDays(1L)
        dailyEvent = mockk()
        event = spyk(
            Event(
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        every { event.id }.returns(1L)
        every { frequency.times }.returns(1)
        every { frequency.subject }.returns(Subject.DAY)
        coEvery {
            fullEventRepository.findByNotebookIdAndLabel(dailyList, label)
        }.returns(dailyEvent)
        val result = defaultEventTrigger.send(event, null, now)
        coVerify(exactly = 0) {
            fullEventRepository.save(any())
        }
        assertEquals(dailyEvent, result)
    }

    @Test
    fun `send, is NOT daily and it has NOT dailyEvent`() = runTest{
        dataBase = now.plusDays(1L)
        event = spyk(
            Event(
                label = label,
                dataBase = dataBase,
                frequency = frequency,
                type = Type.HOME,
            ), recordPrivateCalls = true)
        every { event.id }.returns(1L)
        every { frequency.times }.returns(1)
        every { frequency.subject }.returns(Subject.DAY)
        coEvery {
            fullEventRepository.findByNotebookIdAndLabel(dailyList, label)
        }.returns(dailyEvent)
        val result = defaultEventTrigger.send(event, null, now)
        coVerify(exactly = 0) {
            fullEventRepository.save(any())
        }
        assertNull(result)
    }

}