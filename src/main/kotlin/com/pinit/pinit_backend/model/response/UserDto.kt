package com.pinit.pinit_backend.model.response

import com.pinit.pinit_backend.entity.User

data class UserDto(
    val username: String,
    val firstName: String,
    val lastName: String,
) {
    companion object {
        fun fromEntity(user: User): UserDto {
            return UserDto(
                username = user.username,
                firstName = user.firstName,
                lastName = user.lastName
            )
        }
    }
}
