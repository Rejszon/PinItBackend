package com.pinit.pinit_backend.service

import com.pinit.pinit_backend.entity.Post
import com.pinit.pinit_backend.model.request.CreatePostRequest
import com.pinit.pinit_backend.model.response.PostDto
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
    val userRepository: UserRepository
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


    fun getNearbyPosts(latitude: Double, longitude: Double, radiusInMeters: Double): List<PostDto> {
        val posts = postRepository.findNearbyPosts(latitude, longitude, radiusInMeters)

        return posts.map { PostDto.fromEntity(it) }
    }
}
