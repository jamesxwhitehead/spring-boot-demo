package com.example.demo.service

import com.example.demo.entity.Post
import com.example.demo.entity.Tag
import com.example.demo.exception.ResourceNotFoundException
import com.example.demo.exception.byField
import com.example.demo.exception.byId
import com.example.demo.repository.PostRepository
import com.example.demo.repository.TagRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class TagManagerImplTest(
    @Autowired private val postRepository: PostRepository,
    @Autowired private val tagRepository: TagRepository,
    @Autowired private val tagManager: TagManagerImpl
) {
    @Test
    fun addPostTag() {
        val post = postRepository.findByIdOrNull(POST_ID) ?: fail { ResourceNotFoundException.byId<Post>(POST_ID).toString() }
        val tag = tagRepository.findOrCreate("test")

        assertThat(post.tags.size).isEqualTo(6)
        assertThat(post.tags).doesNotContain(tag)

        tagManager.addPostTag(POST_ID, "test")

        assertThat(post.tags.size).isEqualTo(7)
        assertThat(post.tags).contains(tag)
    }

    @Test
    fun removePostTag() {
        val post = postRepository.findByIdOrNull(POST_ID_15) ?: fail { ResourceNotFoundException.byId<Post>(POST_ID_15).toString() }
        val tag = tagRepository.findByName("Asus") ?: fail { ResourceNotFoundException.byField<Tag>("name", "Asus").toString() }

        assertThat(post.tags.size).isEqualTo(6)
        assertThat(post.tags).contains(tag)

        tagManager.removePostTag(POST_ID_15, "Asus")

        assertThat(post.tags.size).isEqualTo(5)
        assertThat(post.tags).doesNotContain(tag)
        assertThat(tagRepository.findByName("Asus")).isNull()
    }

    companion object {
        private const val POST_ID: Long = 1L
        private const val POST_ID_15: Long = 15L
    }
}
