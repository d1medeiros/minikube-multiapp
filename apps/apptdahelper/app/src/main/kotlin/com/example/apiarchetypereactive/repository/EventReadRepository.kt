package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Event
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
interface EventReadRepository: CoroutineCrudRepository<Event, Long> {
    @Query("select * from event where notebook_id = :notebookId")
    suspend fun findAllByNotebookId(notebookId: Long): List<Event>

}