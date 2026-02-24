package com.example.demo.service

import com.example.demo.entity.Post
import com.example.demo.repository.PostRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class PostPublisherImplTest {
    private val repository = mock<PostRepository>()
    private val postPublisher = PostPublisherImpl(repository)

    @Test
    fun publish() {
        val post = mock<Post>()
        given(repository.findById(1)).willReturn(Optional.of(post))
        given(repository.save(post)).willReturn(post)

        postPublisher.publish(1)

        then(post).should().publish()
        then(repository).should().save(post)
    }

    @Test
    fun archive() {
        val post = mock<Post>()
        given(repository.findById(1)).willReturn(Optional.of(post))
        given(repository.save(post)).willReturn(post)

        postPublisher.archive(1)

        then(post).should().archive()
        then(repository).should().save(post)
    }
}
