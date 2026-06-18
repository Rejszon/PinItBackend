package com.pinit.pinit_backend.service

import com.pinit.pinit_backend.entity.Comment
import com.pinit.pinit_backend.model.request.AddCommentRequest
import com.pinit.pinit_backend.model.response.AuthorDto
import com.pinit.pinit_backend.model.response.CommentDto
import com.pinit.pinit_backend.repository.CommentRepository
import com.pinit.pinit_backend.repository.PostRepository
import com.pinit.pinit_backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun addComment(clerkUserId: String, request: AddCommentRequest): CommentDto {
        val post = postRepository.findById(request.postId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Post doesnt exist") }

        val parentComment = request.parentCommentId?.let {
            commentRepository.findById(it)
                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Parent comment doesnt exist") }
        }
        val user = userRepository.findById(clerkUserId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User doesnt exist") }

        val newComment = Comment(
            post = post,
            user = user,
            content = request.content,
            parentComment = parentComment
        )

        val savedComment = commentRepository.save(newComment)

        return CommentDto(
            id = savedComment.id,
            postId = requireNotNull(savedComment.post.id) { "Post ID cannot be null" },
            author = AuthorDto(user.id, user.username),
            content = savedComment.content,
            parentCommentId = parentComment?.id,
            createdAt = savedComment.createdAt,
        )
    }

    @Transactional(readOnly = true)
    fun getCommentsTree(postId: String): List<CommentDto> {
        val allComments = commentRepository.findAllByPostIdWithUser(postId)

        val dtoMap = allComments.associate { comment ->
            comment.id to CommentDto(
                id = comment.id,
                postId = postId,
                author = AuthorDto(comment.user.id, comment.user.username),
                content = comment.content,
                parentCommentId = comment.parentComment?.id,
                createdAt = comment.createdAt,
            )
        }

        val rootComments = mutableListOf<CommentDto>()

        for (comment in allComments) {
            val currentDto = dtoMap[comment.id]!!
            val parentId = comment.parentComment?.id

            if (parentId == null) {
                rootComments.add(currentDto)
            } else {
                val parentDto = dtoMap[parentId]
                parentDto?.replies?.add(currentDto)
            }
        }

        return rootComments
    }
}
