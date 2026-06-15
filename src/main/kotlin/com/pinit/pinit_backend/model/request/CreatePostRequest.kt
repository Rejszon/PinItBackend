package com.pinit.pinit_backend.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreatePostRequest(
    @field:NotBlank(message = "Content cannot be blank")
    val content: String,

    @field:NotNull(message = "Latitude cannot be blank")
    val latitude: Double,

    @field:NotNull(message = "Longitude cannot be blank")
    val longitude: Double
)
