package com.example.integrationtest

import com.example.apiarchetypereactive.config.Logger
import com.example.apiarchetypereactive.model.Notebook
import com.example.apiarchetypereactive.repository.BookRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [TestConfig::class])
@OptIn(ExperimentalCoroutinesApi::class)
class BookRepositoryTest: IntegrationTest() {

    @Autowired
    lateinit var bookRepository: BookRepository

    @Test
    fun save() = runTest {
        var notebook = Notebook(label = "teste")
        notebook = bookRepository.save(notebook)
        Logger.info("notebook $notebook")
        assertNotNull(notebook)
    }
}

