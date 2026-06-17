package com.pinit.pinit_backend.repository

import com.pinit.pinit_backend.entity.Like
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository: JpaRepository<Like, Long> {
    fun findByPostIdAndUserId(postId: String, userId: String): Like?

    fun countByPostId(postId: String): Long
}
