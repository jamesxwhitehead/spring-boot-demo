package com.example.demo.repository

import com.example.demo.entity.Tag
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Long> {
    fun findByName(name: String): Tag?

    @Throws(DataIntegrityViolationException::class)
    fun findOrCreate(name: String): Tag {
        findByName(name)?.let { return it }

        return try {
            saveAndFlush(Tag(name))
        } catch (exception: DataIntegrityViolationException) {
            findByName(name) ?: throw exception
        }
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
