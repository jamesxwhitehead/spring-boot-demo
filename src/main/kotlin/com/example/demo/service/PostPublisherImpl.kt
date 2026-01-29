package com.example.demo.service

import com.example.demo.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostPublisherImpl(private val repository: PostRepository) : PostPublisher {
    @Transactional
    override fun publish(id: Long) {
        val post = repository.getReferenceById(id)
        post.publish()

        repository.save(post)
    }

    @Transactional
    override fun archive(id: Long) {
        val post = repository.getReferenceById(id)
        post.archive()

        repository.save(post)
    }
}
