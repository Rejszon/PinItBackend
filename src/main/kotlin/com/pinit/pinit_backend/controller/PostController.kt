package com.pinit.pinit_backend.controller

import com.pinit.pinit_backend.model.request.CreatePostRequest
import com.pinit.pinit_backend.model.response.PostDto
import com.pinit.pinit_backend.service.PostService
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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
        @RequestParam(defaultValue = "5000.0") radius: Double,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<List<PostDto>> {
        val clerkUserId = jwt.subject
        val nearbyPosts = postService.getNearbyPosts(lat, lng, radius, clerkUserId)
        return ResponseEntity.ok(nearbyPosts)
    }

    @PostMapping("/{postId}/like")
    fun toggleLike(
        @PathVariable postId: String,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<Map<String, Boolean>> {
        val clerkUserId = jwt.subject
        val isNowLiked = postService.toggleLike(postId, clerkUserId)

        return ResponseEntity.ok(mapOf("liked" to isNowLiked))
    }
}
