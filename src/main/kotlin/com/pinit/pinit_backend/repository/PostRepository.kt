package com.pinit.pinit_backend.repository

import com.pinit.pinit_backend.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, String>{
    @Query(
        value = """
            SELECT * FROM posts 
            WHERE ST_DWithin(
                CAST(location AS geography), 
                CAST(ST_SetSRID(ST_MakePoint(:lng, :lat), 4326) AS geography), 
                :radius
            )
            ORDER BY created_at DESC
        """,
        nativeQuery = true
    )
    fun findNearbyPosts(
        @Param("lat") lat: Double,
        @Param("lng") lng: Double,
        @Param("radius") radiusInMeters: Double
    ): List<Post>
}
