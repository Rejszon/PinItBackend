package com.pinit.pinit_backend.model.response

data class UserStatusResponse(
    val exists: Boolean,
    val user: UserDto? = null
)
