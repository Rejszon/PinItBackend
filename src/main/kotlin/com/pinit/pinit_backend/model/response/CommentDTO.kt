package com.pinit.pinit_backend.model.response

import java.time.LocalDateTime

data class CommentDto(
    val id: String,
    val postId: String,
    val author: AuthorDto,
    val content: String,
    val parentCommentId: String?,
    val createdAt: LocalDateTime,
    val replies: MutableList<CommentDto> = mutableListOf()
)

data class AuthorDto(
    val id: String,
    val username: String? = null,
)
