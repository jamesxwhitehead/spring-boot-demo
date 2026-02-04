package com.example.demo.repository

import com.example.demo.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(name: String): Optional<Tag>

    fun findOrNew(name: String): Tag {
        return findByName(name).orElseGet { Tag(name) }
    }
}
