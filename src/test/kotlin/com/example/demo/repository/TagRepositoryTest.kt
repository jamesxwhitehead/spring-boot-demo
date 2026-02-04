package com.example.demo.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest

@DataJpaTest
class TagRepositoryTest(@Autowired private val repository: TagRepository) {
    @Test
    fun findOrNew() {
        val tag = repository.findOrNew("test")

        assertNull(tag.id)
        assertEquals("test", tag.name)
    }
}
