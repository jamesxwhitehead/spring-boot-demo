package com.example.demo.controller

import com.example.demo.entity.PostFixture
import com.example.demo.entity.TagFixture
import com.example.demo.repository.PostRepository
import com.example.demo.repository.TagRepository
import net.datafaker.Faker
import org.assertj.core.api.Assertions.assertThat
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
class PostTagControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val faker: Faker,
    @Autowired private val postRepository: PostRepository,
    @Autowired private val tagRepository: TagRepository
) {
    @Test
    fun update() {
        val post = PostFixture.withTag(faker)
        postRepository.save(post)
        val tag = TagFixture.new(faker)

        val path = "/posts/${post.id}/tags/${tag.name}"

        mockMvc.put(path)
            .andExpectAll {
                status { isNoContent() }
            }

        assertThat(post.tags).flatExtracting({ it.name }).contains(tag.name)
        assertThat(tagRepository.findByName(tag.name)).isNotNull()
    }

    @Test
    fun destroy() {
        val post = PostFixture.withTag(faker)
        postRepository.save(post)
        val tag = post.tags.first()

        val path = "/posts/${post.id}/tags/${tag.name}"

        mockMvc.delete(path)
            .andExpectAll {
                status { isNoContent() }
            }

        assertThat(post.tags).flatExtracting({ it.name }).doesNotContain(tag.name)
        assertThat(tagRepository.findByName(tag.name)).isNull()
    }
}
