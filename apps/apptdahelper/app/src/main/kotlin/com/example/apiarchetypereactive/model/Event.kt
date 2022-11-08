package com.example.apiarchetypereactive.model

import com.example.apiarchetypereactive.config.toBoolean
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.PersistenceCreator
import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table
data class Event(
    @field:Id
    val id: Long? = null,
    val label: String,
    var dataBase: LocalDateTime,
    @field:Transient
    var frequency: Frequency? = null,
    var active: Boolean = false,
    val type: Type,
    var notebookId: Long? = null,
) {

    @PersistenceCreator
    constructor(
        id: Long? = null,
        label: String,
        dataBase: LocalDateTime,
        active: Byte,
        type: Type,
        notebookId: Long? = null,
    ) : this(
        id, label, dataBase, null, active.toBoolean(), type, notebookId
    ){
        this.frequency = null
    }

}


