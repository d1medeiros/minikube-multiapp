package com.example.apiarchetypereactive.extension

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class UtilKtTest {


    @Test
    fun `isAfterOrEqual, normal`() {
        val now = LocalDateTime.now()
        val dataBase = now.minusDays(1L)
        val result = now.isAfterOrEqual(dataBase)
        assertTrue(result)
    }

    @Test
    fun `isAfterOrEqual, equal`() {
        val now = LocalDateTime.now()
        val dataBase = now
        val result = now.isAfterOrEqual(dataBase)
        assertTrue(result)
    }

    @Test
    fun isLimit() {
        val now = LocalDateTime.now()
        val dataBase = now.minusWeeks(1L)
        val result = dataBase.isLimit(now)
        assertTrue(result)
    }

}