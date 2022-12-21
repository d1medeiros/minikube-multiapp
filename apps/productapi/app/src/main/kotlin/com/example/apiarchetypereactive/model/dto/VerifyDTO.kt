package com.example.apiarchetypereactive.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class VerifyDTO(
    val valid: Boolean,
    val internalCode: String
)