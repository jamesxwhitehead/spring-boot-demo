package com.example.demo.service

import com.example.demo.entity.Post
import com.example.demo.entity.PostState
import com.example.demo.exception.PostStateTransitionNotAllowedException
import com.example.demo.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostPublisherImpl(private val repository: PostRepository) : PostPublisher {
    @Transactional
    override fun publish(id: Long): Post {
        val post = repository.getReferenceById(id)

        try {
            post.publish()
        } catch (exception: IllegalStateException) {
            throw PostStateTransitionNotAllowedException(
                ERROR_DETAIL.format(post.state, PostState.PUBLISHED),
                exception
            )
        }

        return repository.save(post)
    }

    @Transactional
    override fun archive(id: Long): Post {
        val post = repository.getReferenceById(id)

        try {
            post.archive()
        } catch (exception: IllegalStateException) {
            throw PostStateTransitionNotAllowedException(
                ERROR_DETAIL.format(post.state, PostState.ARCHIVED),
                exception
            )
        }

        return repository.save(post)
    }

    private companion object {
        const val ERROR_DETAIL: String = "Operation violates post lifecycle rules: %s -> %s is not permitted."
    }
}
