package com.example.apiarchetypereactive.extension

import com.example.apiarchetypereactive.model.Frequency
import com.example.apiarchetypereactive.model.Subject
import java.time.LocalDateTime


fun LocalDateTime.plus(frequency: Frequency): LocalDateTime {
    return when (frequency.subject) {
        Subject.DAY -> this.plusDays(frequency.times.toLong())
        Subject.WEEK -> this.plusWeeks(frequency.times.toLong())
        Subject.MONTH -> this.plusMonths(frequency.times.toLong())
        Subject.YEAR -> this.plusYears(frequency.times.toLong())
        else -> this
    }
}