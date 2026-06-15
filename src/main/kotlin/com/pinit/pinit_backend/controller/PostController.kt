package com.pinit.pinit_backend.controller

import com.pinit.pinit_backend.model.request.CreatePostRequest
import com.pinit.pinit_backend.model.response.PostDto
import com.pinit.pinit_backend.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/posts")
class PostController(private val postService: PostService) {
    @PostMapping("/create-post")
    fun createPost(
        @RequestBody @Validated request: CreatePostRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<PostDto> {
        val clerkUserId = jwt.subject
        val savedPost = postService.createPost(clerkUserId, request)

        return ResponseEntity.ok(PostDto.fromEntity(savedPost))
    }

    @GetMapping("/nearby")
    fun getNearbyPosts(
        @RequestParam lat: Double,
        @RequestParam lng: Double,
        @RequestParam(defaultValue = "5000.0") radius: Double
    ): ResponseEntity<List<PostDto>> {
        val nearbyPosts = postService.getNearbyPosts(lat, lng, radius)
        return ResponseEntity.ok(nearbyPosts)
    }
}
