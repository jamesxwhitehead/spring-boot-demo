package com.example.demo.repository

import com.example.demo.entity.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(name: String): Tag?

    fun findOrNew(name: String): Tag {
        return findByName(name) ?: Tag(name)
    }

    @Modifying
    @Query(
        """
        delete from Tag t
        where t.id = :tagId
        and t._posts is empty
        """
    )
    fun deleteIfOrphaned(@Param("tagId") tagId: Long): Int
}
