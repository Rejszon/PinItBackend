package com.pinit.pinit_backend.repository

import com.pinit.pinit_backend.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PostRepository : JpaRepository<Post, String>{
    @Query(
        value = """
            SELECT 
                p.id AS id,
                p.content AS content,
                ST_Y(p.location::geometry) AS latitude,
                ST_X(p.location::geometry) AS longitude,
                u.username AS "authorUsername",
                p.created_at AS "createdAt",
                (SELECT CAST(COUNT(*) AS INTEGER) FROM likes l WHERE l.post_id = p.id) AS "likesCount",
                EXISTS(SELECT 1 FROM likes l WHERE l.post_id = p.id AND l.user_id = :currentUserId) AS "likedByMe"
            FROM posts p
            JOIN users u ON p.user_id = u.id
            WHERE ST_DWithin(
                CAST(p.location AS geography), 
                CAST(ST_SetSRID(ST_MakePoint(:lng, :lat), 4326) AS geography), 
                :radius
            )
            ORDER BY p.created_at DESC
        """,
        nativeQuery = true
    )
    fun findNearbyPostsForFeed(
        @Param("lat") lat: Double,
        @Param("lng") lng: Double,
        @Param("radius") radiusInMeters: Double,
        @Param("currentUserId") currentUserId: String
    ): List<PostWithLikesProjection>
}

interface PostWithLikesProjection {
    val id: String
    val content: String
    val latitude: Double
    val longitude: Double
    val authorUsername: String
    val createdAt: LocalDateTime
    val likesCount: Int
    val likedByMe: Boolean
}
