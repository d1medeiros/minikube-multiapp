package com.example.apiarchetypereactive.repository

import com.example.apiarchetypereactive.model.Notebook
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface NotebookRepository: CoroutineCrudRepository<Notebook, Long> {
}