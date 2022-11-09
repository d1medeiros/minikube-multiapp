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

fun LocalDateTime.isAfterOrEqual(date: LocalDateTime): Boolean {
    return this.isAfter(date)
        .or(this.isEqual(date))
}

fun LocalDateTime.isLimit(date: LocalDateTime): Boolean {
    val plusWeeks = this.plusWeeks(1L)
    return date.isAfterOrEqual(plusWeeks)


}

fun Frequency?.getNextDateTime(dataBase: LocalDateTime): LocalDateTime {
   return this?.let {
        dataBase.plus(it)
    } ?: throw Exception("frequency is null")
}


fun Byte.toBoolean(): Boolean {
    return when (this) {
        0.toByte() -> true
        else -> false
    }
}
fun Boolean.toByte(): Byte {
    return when (this) {
        true -> 0.toByte()
        else -> 1.toByte()
    }
}