package com.example.demo.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest

@DataJpaTest
class TagRepositoryTest(@Autowired private val repository: TagRepository) {
    @Test
    fun findOrCreate() {
        val tag = repository.findOrCreate("test")

        assertNotNull(tag.id)
        assertEquals("test", tag.name)
    }
}
