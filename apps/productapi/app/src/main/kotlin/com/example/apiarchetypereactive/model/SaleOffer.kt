package com.example.apiarchetypereactive.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class SaleOffer(
    val id: String,
    val eligible: Boolean,
    @Contextual
    val limit: BigDecimal,
    val internalCode: String
)
