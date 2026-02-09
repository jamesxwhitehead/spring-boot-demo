package com.example.demo.controller

import com.example.demo.entity.PostFixture
import com.example.demo.entity.PostState
import com.example.demo.repository.PostRepository
import com.fasterxml.jackson.databind.ObjectMapper
import net.datafaker.Faker
import org.hamcrest.Matchers.containsString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val faker: Faker,
    @Autowired private val repository: PostRepository
) {
    private val objectMapper = ObjectMapper()

    @Test
    fun index() {
        mockMvc.get("/posts")
            .andExpectAll {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") { isArray() }
                jsonPath("$.length()") { value(10) }
            }
    }

    @Test
    fun store() {
        val post = PostFixture.create(faker)

        mockMvc.post("/posts") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(post)
        }.andExpectAll {
            status { isCreated() }
            header { string("Location", containsString("/posts/")) }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { isNumber() }
            jsonPath("$.author") { value(post.author) }
            jsonPath("$.title") { value(post.title) }
            jsonPath("$.content") { value(post.content) }
            jsonPath("$.publishedAt") { value(null) }
            jsonPath("$.state") { value(PostState.DRAFT.name) }
            jsonPath("$.tags") { isArray() }
            jsonPath("$.version") { isNumber() }
        }
    }

    @Test
    fun storeShouldReturnBadRequestWhenRequiredFieldIsNull() {
        val post = mapOf(
            "author" to null,
            "title" to null,
            "content" to null
        )

        mockMvc.post("/posts") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(post)
        }.andExpectAll {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            jsonPath("$.detail") { exists() }
            jsonPath("$.instance") { value("/posts") }
            jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.title") { value(HttpStatus.BAD_REQUEST.reasonPhrase) }
        }
    }

    @Test
    fun storeShouldReturnBadRequestWhenRequiredFieldIsBlank() {
        val post = PostFixture.empty()

        mockMvc.post("/posts") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(post)
        }.andExpectAll {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            jsonPath("$.detail") { exists() }
            jsonPath("$.instance") { value("/posts") }
            jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.title") { value(HttpStatus.BAD_REQUEST.reasonPhrase) }
        }
    }

    @Test
    fun show() {
        val path = "/posts/${POST_ID}"

        mockMvc.get(path)
            .andExpectAll {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(POST_ID) }
                jsonPath("$.author") { value("Phebe Lehner") }
                jsonPath("$.title") { value("Gone with the Wind") }
                jsonPath("$.content") { exists() }
                jsonPath("$.publishedAt") { value(null) }
                jsonPath("$.state") { value(PostState.DRAFT.name) }
                jsonPath("$.tags") { isArray() }
                jsonPath("$.tags.length()") { value(2) }
                jsonPath("$.version") { isNumber() }
            }
    }

    @Test
    fun showShouldReturnNotFoundWhenPostDoesNotExist() {
        val path = "/posts/${Long.MAX_VALUE}"

        mockMvc.get(path)
            .andExpectAll {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
                jsonPath("$.detail") { value("Post not found (id=${Long.MAX_VALUE})") }
                jsonPath("$.instance") { value(path) }
                jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
                jsonPath("$.title") { value(HttpStatus.NOT_FOUND.reasonPhrase) }
            }
    }

    @Test
    fun publish() {
        val path = "/posts/${POST_ID}/publish"

        mockMvc.patch(path)
            .andExpectAll {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(POST_ID) }
                jsonPath("$.publishedAt") { isMap() }
                jsonPath("$.publishedAt.epochSeconds") { exists() }
                jsonPath("$.publishedAt.nanosecondsOfSecond") { exists() }
                jsonPath("$.state") { value(PostState.PUBLISHED.name) }
            }
    }

    @Test
    fun publishShouldReturnNotFoundWhenPostDoesNotExist() {
        val path = "/posts/${Long.MAX_VALUE}/publish"

        mockMvc.patch(path)
            .andExpectAll {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
                jsonPath("$.detail") { value("Post not found (id=${Long.MAX_VALUE})") }
                jsonPath("$.instance") { value(path) }
                jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
                jsonPath("$.title") { value(HttpStatus.NOT_FOUND.reasonPhrase) }
            }
    }

    @Test
    fun publishShouldReturnUnprocessableContentWhenPostCannotBePublished() {
        val post = repository.getReferenceById(POST_ID)
        post.publish()
        repository.save(post)

        val path = "/posts/${POST_ID}/publish"

        mockMvc.patch(path)
            .andExpectAll {
                status { isUnprocessableContent() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
                jsonPath("$.detail") { value("Operation violates post lifecycle rules: PUBLISHED -> PUBLISHED is not permitted.") }
                jsonPath("$.instance") { value(path) }
                jsonPath("$.status") { value(HttpStatus.UNPROCESSABLE_CONTENT.value()) }
                jsonPath("$.title") { value(HttpStatus.UNPROCESSABLE_CONTENT.reasonPhrase) }
            }
    }

    @Test
    fun archive() {
        val path = "/posts/${POST_ID}/archive"

        mockMvc.patch(path)
            .andExpectAll {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(POST_ID) }
                jsonPath("$.publishedAt") { value(null) }
                jsonPath("$.state") { value(PostState.ARCHIVED.name) }
            }
    }

    @Test
    fun archiveShouldReturnNotFoundWhenPostDoesNotExist() {
        val path = "/posts/${Long.MAX_VALUE}/archive"

        mockMvc.patch(path)
            .andExpectAll {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
                jsonPath("$.detail") { value("Post not found (id=${Long.MAX_VALUE})") }
                jsonPath("$.instance") { value(path) }
                jsonPath("$.status") { value(HttpStatus.NOT_FOUND.value()) }
                jsonPath("$.title") { value(HttpStatus.NOT_FOUND.reasonPhrase) }
            }
    }

    @Test
    fun archiveShouldReturnUnprocessableContentWhenPostCannotBeArchived() {
        val post = repository.getReferenceById(POST_ID)
        post.archive()
        repository.save(post)

        val path = "/posts/${POST_ID}/archive"

        mockMvc.patch(path)
            .andExpectAll {
                status { isUnprocessableContent() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
                jsonPath("$.detail") { value("Operation violates post lifecycle rules: ARCHIVED -> ARCHIVED is not permitted.") }
                jsonPath("$.instance") { value(path) }
                jsonPath("$.status") { value(HttpStatus.UNPROCESSABLE_CONTENT.value()) }
                jsonPath("$.title") { value(HttpStatus.UNPROCESSABLE_CONTENT.reasonPhrase) }
            }
    }

    private companion object {
        const val POST_ID: Long = 1L
    }
}
