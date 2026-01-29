package com.example.demo.controller

import com.example.demo.entity.Post
import com.example.demo.repository.PostRepository
import com.example.demo.service.PostPublisherImpl
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(PostController::class)
@Import(PostPublisherImpl::class)
class PostControllerTest(@Autowired private val mockMvc: MockMvc) {
    @MockitoBean
    lateinit var repository: PostRepository

    @MockitoSpyBean
    lateinit var publisher: PostPublisherImpl

    @Test
    fun publish_returns422ProblemJson_andDoesNotSave() {
        givenPostActionThrows { publish() }

        assertUnprocessablePatch("/posts/$POST_ID/publish")

        then(repository).should(never()).save(any())
    }

    @Test
    fun archive_returns422ProblemJson_andDoesNotSave() {
        givenPostActionThrows { archive() }

        assertUnprocessablePatch("/posts/$POST_ID/archive")

        then(repository).should(never()).save(any())
    }

    private fun givenPostActionThrows(action: Post.() -> Unit) {
        val post = mock(Post::class.java)

        given(repository.getReferenceById(POST_ID)).willReturn(post)
        given(post.action()).willThrow(IllegalStateException(ERROR_DETAIL))
    }

    private fun assertUnprocessablePatch(path: String) {
        mockMvc
            .perform(patch(path))
            .andExpectAll(
                status().isUnprocessableContent,
                content().contentType(MediaType.APPLICATION_PROBLEM_JSON),
                jsonPath("$.detail").value(ERROR_DETAIL),
                jsonPath("$.instance").value(path),
                jsonPath("$.status").value(HttpStatus.UNPROCESSABLE_CONTENT.value()),
                jsonPath("$.title").value(HttpStatus.UNPROCESSABLE_CONTENT.reasonPhrase)
            )
    }

    private companion object {
        const val POST_ID: Long = 1L
        const val ERROR_DETAIL: String = "Check failed."
    }
}
