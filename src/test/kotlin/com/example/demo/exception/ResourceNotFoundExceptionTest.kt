package com.example.demo.exception

import com.example.demo.entity.Post
import com.example.demo.entity.Tag
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ResourceNotFoundExceptionTest {
    @Test
    fun byId() {
        val exception = ResourceNotFoundException.byId<Post>(1L)

        val message = exception.message

        assertThat(message).isEqualTo("Post not found (id=1)")
    }

    @Test
    fun byField() {
        val exception = ResourceNotFoundException.byField<Tag>("name", "test")

        val message = exception.message

        assertThat(message).isEqualTo("Tag not found (name=test)")
    }
}
