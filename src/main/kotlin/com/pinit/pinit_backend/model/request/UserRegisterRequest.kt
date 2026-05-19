package com.pinit.pinit_backend.model.request

import jakarta.validation.constraints.NotBlank

data class UserRegisterRequest(
    @field:NotBlank(message = "Username cannot be blank")
    val username: String,

    @field:NotBlank(message = "First name cannot be blank")
    val firstName: String,

    @field:NotBlank(message = "Last name cannot be blank")
    val lastName: String
)
