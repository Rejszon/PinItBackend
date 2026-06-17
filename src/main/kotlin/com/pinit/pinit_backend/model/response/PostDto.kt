package com.pinit.pinit_backend.model.response

import com.pinit.pinit_backend.entity.Post
import java.time.LocalDateTime

data class PostDto(
    val id: String?,
    val content: String,
    val latitude: Double,
    val longitude: Double,
    val authorUsername: String,
    val createdAt: LocalDateTime,
    val likesCount: Int,
    val likedByMe: Boolean
) {
    companion object {
        fun fromEntity(post: Post, currentUserId: String? = null): PostDto {
            return PostDto(
                id = post.id,
                content = post.content,
                latitude = post.location.y,
                longitude = post.location.x,
                authorUsername = post.user.username,
                createdAt = post.createdAt,
                likesCount = post.likes.size,
                likedByMe = currentUserId != null && post.likes.any { it.userId == currentUserId }
            )
        }
    }
}
