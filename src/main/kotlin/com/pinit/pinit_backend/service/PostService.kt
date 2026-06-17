package com.pinit.pinit_backend.service

import com.pinit.pinit_backend.entity.Like
import com.pinit.pinit_backend.entity.Post
import com.pinit.pinit_backend.model.request.CreatePostRequest
import com.pinit.pinit_backend.model.response.PostDto
import com.pinit.pinit_backend.repository.LikeRepository
import com.pinit.pinit_backend.repository.PostRepository
import com.pinit.pinit_backend.repository.UserRepository
import jakarta.transaction.Transactional
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.PrecisionModel
import org.springframework.stereotype.Service

@Service
class PostService(
    val postRepository: PostRepository,
    val userRepository: UserRepository,
    val likeRepository: LikeRepository,
) {
    private val geometryFactory = GeometryFactory(PrecisionModel(), 4326)
    @Transactional
    fun createPost(clerkUserId: String,request: CreatePostRequest): Post {
        val user = userRepository.findById(clerkUserId).orElseThrow {
            IllegalArgumentException("Missing user")
        }
        val locationPoint = geometryFactory.createPoint(Coordinate(request.longitude, request.latitude))

        val newPost = Post(
            user = user,
            content = request.content,
            location = locationPoint
        )
        return postRepository.save(newPost)
    }

    fun getNearbyPosts(lat: Double, lng: Double, radius: Double, currentUserId: String): List<PostDto> {
        val projections = postRepository.findNearbyPostsForFeed(lat, lng, radius, currentUserId)

        return projections.map { proj ->
            PostDto(
                id = proj.id,
                content = proj.content,
                latitude = proj.latitude,
                longitude = proj.longitude,
                authorUsername = proj.authorUsername,
                createdAt = proj.createdAt,
                likesCount = proj.likesCount,
                likedByMe = proj.likedByMe,
            )
        }
    }

    fun toggleLike(postId: String, clerkUserId: String): Boolean {
        val post = postRepository.findById(postId).orElseThrow {
            IllegalArgumentException("Post not found")
        }

        val existingLike = likeRepository.findByPostIdAndUserId(postId, clerkUserId)

        return if (existingLike != null) {
            likeRepository.delete(existingLike)
            false
        } else {
            val newLike = Like(post = post, userId = clerkUserId)
            likeRepository.save(newLike)
            true
        }
    }
}
