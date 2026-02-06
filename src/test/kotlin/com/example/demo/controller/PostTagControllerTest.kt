package com.example.demo.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.put
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PostTagControllerTest(@Autowired private val mockMvc: MockMvc) {
    @Test
    fun update() {
        val path = "/posts/${POST_ID}/tags/test"

        mockMvc.put(path)
            .andExpectAll {
                status { isNoContent() }
            }
    }

    @Test
    fun destroy() {
        val path = "/posts/${POST_ID}/tags/Western"

        mockMvc.delete(path)
            .andExpectAll {
                status { isNoContent() }
            }
    }

    private companion object {
        const val POST_ID: Long = 1L
    }
}
