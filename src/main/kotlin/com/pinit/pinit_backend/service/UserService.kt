package com.pinit.pinit_backend.service

import com.pinit.pinit_backend.entity.User
import com.pinit.pinit_backend.model.request.UserRegisterRequest
import com.pinit.pinit_backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional
    fun signup(clerkUserId: String, request: UserRegisterRequest): User {
        if (userRepository.existsById(clerkUserId)) {
            throw IllegalArgumentException("User already exists")
        }

        val newUser = User(
            id = clerkUserId,
            username = request.username,
            firstName = request.firstName,
            lastName = request.lastName
        )

        return userRepository.save(newUser)
    }

    fun getUserProfile(clerkUserId: String): User? {
        return userRepository.findById(clerkUserId).orElse(null)
    }
}
