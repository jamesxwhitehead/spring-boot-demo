package com.example.demo.controller

import com.example.demo.dto.request.CreateUserRequestDtoFixture
import com.example.demo.entity.User
import com.example.demo.entity.UserFixture
import com.example.demo.repository.UserRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
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
import tools.jackson.databind.ObjectMapper

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val faker: Faker,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val repository: UserRepository
) {
    @Test
    fun index() {
        mockMvc.get("/users")
            .andExpectAll {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") { isArray() }
                jsonPath("$.length()") { value(5) }
            }
    }

    @Test
    fun store() {
        val dto = CreateUserRequestDtoFixture.create(faker)

        mockMvc.post("/users") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(dto)
        }.andExpectAll {
            status { isCreated() }
            header { string("Location", containsString("/users/")) }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.id") { isNumber() }
            jsonPath("$.username") { value(dto.username) }
            jsonPath("$.password") { value(dto.password) }
            jsonPath("$.account") { isMap() }
            jsonPath("$.account.accountExpired") { value(false) }
            jsonPath("$.account.accountLocked") { value(false) }
            jsonPath("$.account.credentialsExpired") { value(false) }
            jsonPath("$.account.disabled") { value(false) }
            jsonPath("$.roles") { isArray() }
            jsonPath("$.roles.length()") { value(1) }
            jsonPath("$.roles[0]") { value("ROLE_USER") }
        }
    }

    @Test
    fun storeShouldReturnBadRequestWhenRequiredFieldIsNull() {
        val dto = CreateUserRequestDtoFixture.nullFields()

        mockMvc.post("/users") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(dto)
        }.andExpectAll {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            jsonPath("$.detail") { exists() }
            jsonPath("$.instance") { value("/users") }
            jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.title") { value(HttpStatus.BAD_REQUEST.reasonPhrase) }
        }
    }

    @Test
    fun storeShouldReturnBadRequestWhenRequiredFieldIsBlank() {
        val dto = CreateUserRequestDtoFixture.blank()

        mockMvc.post("/users") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(dto)
        }.andExpectAll {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            jsonPath("$.detail") { exists() }
            jsonPath("$.instance") { value("/users") }
            jsonPath("$.status") { value(HttpStatus.BAD_REQUEST.value()) }
            jsonPath("$.title") { value(HttpStatus.BAD_REQUEST.reasonPhrase) }
        }
    }

    @Test
    fun storeShouldReturnConflictWhenUserExists() {
        val dto = CreateUserRequestDtoFixture.create(faker)
        val user = User.fromDto(dto)
        repository.save(user)

        mockMvc.post("/users") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(dto)
        }.andExpectAll {
            status { isConflict() }
        }
    }

    @Test
    fun show() {
        val path = "/users/${USER_ID}"

        mockMvc.get(path)
            .andExpectAll {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.id") { value(USER_ID) }
                jsonPath("$.username") { value("angelina.howell") }
                jsonPath("$.password") { value("9a4349b5x") }
                jsonPath("$.account") { isMap() }
                jsonPath("$.account.accountExpired") { isBoolean() }
                jsonPath("$.account.accountLocked") { isBoolean() }
                jsonPath("$.account.credentialsExpired") { isBoolean() }
                jsonPath("$.account.disabled") { isBoolean() }
                jsonPath("$.roles") { isArray() }
            }
    }

    @Test
    fun disable() {
        val user = UserFixture.create(faker)
        repository.save(user)

        mockMvc.patch("/users/${user.id}/disable")
            .andExpectAll {
                status { isNoContent() }
            }

        assertThat(user.account.disabled).isTrue()
    }

    @Test
    fun enable() {
        val user = UserFixture.disabled(faker)
        repository.save(user)

        mockMvc.patch("/users/${user.id}/enable")
            .andExpectAll {
                status { isNoContent() }
            }

        assertThat(user.account.disabled).isFalse()
    }

    companion object {
        private const val USER_ID: Long = 1L
    }
}
