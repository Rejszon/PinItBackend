package com.pinit.pinit_backend.controller

import com.pinit.pinit_backend.model.request.AddCommentRequest
import com.pinit.pinit_backend.service.CommentService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/comments")
class CommentController(private val commentService: CommentService) {

    @PostMapping
    fun addComment(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestBody request: AddCommentRequest
    ): ResponseEntity<*> {
        val userId = jwt.subject

        val comment = commentService.addComment(
            clerkUserId = userId,
            request = request,
        )

        return ResponseEntity.ok(comment)
    }

    @GetMapping("/post/{postId}")
    fun getComments(@PathVariable postId: String) =
        ResponseEntity.ok(commentService.getCommentsTree(postId))
}
