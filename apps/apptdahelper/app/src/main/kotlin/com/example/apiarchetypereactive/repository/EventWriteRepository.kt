package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Event
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface EventWriteRepository: CoroutineCrudRepository<Event, Long>{
    @Query("select * from event where notebook_id = :notebookId")
    suspend fun findAllByNotebookId(notebookId: Long): List<Event>

    @Query("select count(*) from event where notebook_id = :notebookId and label = :label")
    suspend fun existsByNotebookIdAndLabel(notebookId: Long, label: String): Long

    @Query("select * from event where notebook_id = :notebookId and label = :label")
    suspend fun findByNotebookIdAndLabel(notebookId: Long, label: String): Event?
}



