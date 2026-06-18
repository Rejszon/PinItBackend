package com.pinit.pinit_backend.model.request

import jakarta.validation.constraints.NotBlank

data class AddCommentRequest(
    @field:NotBlank(message = "Content cannot be blank")
    val content: String,

    @field:NotBlank(message = "Post ID cannot be blank")
    val postId: String,

    val parentCommentId: String? = null,
)
