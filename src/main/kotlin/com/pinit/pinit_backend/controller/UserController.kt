package com.pinit.pinit_backend.controller

import com.pinit.pinit_backend.model.request.UserRegisterRequest
import com.pinit.pinit_backend.model.response.UserDto
import com.pinit.pinit_backend.model.response.UserStatusResponse
import com.pinit.pinit_backend.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService
) {

    @GetMapping("/me")
    fun getCurrentUser(
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<UserStatusResponse> {
        val clerkUserId = jwt.subject
        val userEntity = userService.getUserProfile(clerkUserId)

        return if (userEntity != null) {
            val safeProfile = UserDto.fromEntity(userEntity)
            ResponseEntity.ok(UserStatusResponse(exists = true, user = safeProfile))
        } else {
            ResponseEntity.ok(UserStatusResponse(exists = false, user = null))
        }
    }

    @PostMapping("/sign-up")
    fun signup(
        @RequestBody @Validated request: UserRegisterRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<UserDto> {
        val clerkUserId = jwt.subject
        val savedUserEntity = userService.signup(clerkUserId, request)

        return ResponseEntity.ok(UserDto.fromEntity(savedUserEntity))
    }
}
