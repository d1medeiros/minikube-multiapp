package com.example.apiarchetypereactive.controller

import com.example.apiarchetypereactive.component.EventDefault
import com.example.apiarchetypereactive.component.EventManager
import com.example.apiarchetypereactive.component.TimeControl
import com.example.apiarchetypereactive.model.Event
import com.example.apiarchetypereactive.model.Frequency
import com.example.apiarchetypereactive.model.Subject
import com.example.apiarchetypereactive.model.Type
import com.example.apiarchetypereactive.repository.FullEventRepository
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Commit
import java.time.LocalDateTime

@SpringBootTest
class EventManagerTest {
    @Autowired
    lateinit var fullEventRepository: FullEventRepository

    lateinit var now: LocalDateTime
    @Commit
    @BeforeEach
    internal fun setUp() {
        runBlocking {
            fullEventRepository.deleteAll()
            now = LocalDateTime.now()
            val baseEvents = baseEvents(now)
//            fullEventRepository.save(baseEvents[0])
            fullEventRepository.saveAll(baseEvents.asFlow())
            println("finishing setUp run")
        }
        println("finishing setUp")
    }

    @Test
    fun `test 1`() {
        runBlocking {
            val eventTrigger = EventDefault()
            val timeControl = TimeControl(eventTrigger)
            val eventManager = EventManager(
                fullEventRepository = fullEventRepository,
                timeControl = timeControl
            )
            eventManager.run(now.plusDays(1))

        }
    }

    internal fun save() {
        val count = runBlocking {
            val e = fullEventRepository.save(Event(
                label = "teste",
                dataBase = LocalDateTime.now(),
                type = Type.HOME,
                active = false,
                frequency = Frequency(
                    times = 1,
                    subject = Subject.DAY
                )
            ))
            println(e.id)
            fullEventRepository.count()
        }
        Assertions.assertEquals(1, count)
    }

    internal fun find() {
        val result = runBlocking {
            val e = fullEventRepository.findById(1)
            println(e)
            e
        }
        assertNotNull(result)
    }

   /*
*/
   private fun baseEvents(now: LocalDateTime): List<Event> {
        return eventsHealth(now) + eventsCar(now) + eventsHome(now) + eventsPet(now)
    }

    private fun eventsHealth(dataBase: LocalDateTime): List<Event> {
        return listOf(
            Event(
                label = "Checkup",
                dataBase = dataBase,
                frequency = Frequency(
                    times = 6,
                    subject = Subject.MONTH
                ),
                active = false,
                type = Type.HEALTH,
            ),
        )
    }

    private fun eventsCar(dataBase: LocalDateTime): List<Event> {
        return listOf(
            Event(
                label = "Pagar IPVA",
                dataBase = dataBase,
                frequency = Frequency(
                    times = 1,
                    subject = Subject.YEAR
                ),
                active = false,
                type = Type.CAR,
            ),
            Event(
                label = "Lavar Carro",
                dataBase = dataBase,
                frequency = Frequency(
                    times=2,
                    subject = Subject.WEEK
                ),
                active = false,
                type = Type.CAR,
            ),
        )
    }

    private fun eventsPet(dataBase: LocalDateTime): List<Event> {
        return listOf(
            Event(
                label = "Dar comida",
                dataBase = dataBase,
                frequency = Frequency(
                    times = 1,
                    subject = Subject.DAY
                ),
                active = false,
                type = Type.PET,
            ),
            Event(
                label = "Dar agua",
                dataBase = dataBase,
                frequency = Frequency(
                    times = 1,
                    subject = Subject.DAY
                ),
                active = false,
                type = Type.PET,
            ),
            Event(
                label = "Brincar",
                dataBase = dataBase,
                frequency = Frequency(
                    times = 1,
                    subject = Subject.DAY
                ),
                active = false,
                type = Type.PET,
            ),
            Event(
                label = "Limpar banheiro",
                dataBase = dataBase,
                frequency = Frequency(
                    times = 1,
                    subject = Subject.DAY
                ),
                active = false,
                type = Type.PET,
            ),
        )
    }

    private fun eventsHome(dataBase: LocalDateTime): List<Event> {
        return listOf(
            Event(
                label = "Lavar louça",
                dataBase = dataBase,
                frequency = Frequency(
                    times = 1,
                    subject = Subject.DAY
                ),
                active = false,
                type = Type.HOME,
            ),
            Event(
                label = "Faxina",
                dataBase = dataBase,
                frequency = Frequency(
                    times = 1,
                    subject = Subject.WEEK
                ),
                active = false,
                type = Type.HOME,
            ),
        )
    }
}