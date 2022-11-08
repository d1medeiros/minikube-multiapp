package com.example.apiarchetypereactive.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Notebook(
    @field:Id
    var id: Long? = null,
    val label: String,
)

