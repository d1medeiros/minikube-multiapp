package com.example.apiarchetypereactive.model

import com.example.apiarchetypereactive.annotation.MOpen
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
@MOpen
data class Frequency(
    @Id
    var id: Long? = null,
    var times: Int,
    var subject: Subject,
    var eventId: Long? = null,
)