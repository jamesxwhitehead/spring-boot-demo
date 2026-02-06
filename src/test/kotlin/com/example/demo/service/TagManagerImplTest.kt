package com.example.demo.service

import com.example.demo.repository.PostRepository
import com.example.demo.repository.TagRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
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
        val post = postRepository.findByIdOrNull(POST_ID)!!
        val tag = tagRepository.findByName("Textbook")!!

        assertThat(post.tags.size).isEqualTo(2)
        assertThat(post.tags).doesNotContain(tag)

        tagManager.addPostTag(POST_ID, "Textbook")

        assertThat(post.tags.size).isEqualTo(3)
        assertThat(post.tags).contains(tag)
    }

    @Test
    fun removePostTag() {
        val post = postRepository.findByIdOrNull(POST_ID)!!
        val tag = tagRepository.findByName("Western")!!

        assertThat(post.tags.size).isEqualTo(2)
        assertThat(post.tags).contains(tag)

        tagManager.removePostTag(POST_ID, "Western")

        assertThat(post.tags.size).isEqualTo(1)
        assertThat(post.tags).doesNotContain(tag)
        assertThat(tagRepository.findByName("Western")).isNull()
    }

    private companion object {
        const val POST_ID: Long = 1L
    }
}
