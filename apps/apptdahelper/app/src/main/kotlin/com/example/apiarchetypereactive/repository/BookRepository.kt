package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Notebook
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Repository

@Repository
class BookRepository(
    private val notebookRepository: NotebookRepository
){
    suspend fun save(notebook: Notebook): Notebook {
        return notebookRepository.save(notebook)
    }

    suspend fun deleteAll() {
        notebookRepository.deleteAll()
    }

    suspend fun findAll(): List<Notebook> {
        return notebookRepository.findAll().toList()
    }
}