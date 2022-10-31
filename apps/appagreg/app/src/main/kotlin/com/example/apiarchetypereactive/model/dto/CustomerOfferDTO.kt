package com.example.apiarchetypereactive.model.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CustomerOfferDTO(
    val id: String,
    val eligible: Boolean,
    @Contextual
    val limit: BigDecimal
)
