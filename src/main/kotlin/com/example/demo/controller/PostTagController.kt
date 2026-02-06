package com.example.demo.controller

import com.example.demo.service.TagManager
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts/{id}/tags/{name}")
class PostTagController(private val tagManager: TagManager) {
    @PutMapping
    fun update(@PathVariable id: Long, @PathVariable name: String): ResponseEntity<Unit> {
        tagManager.addPostTag(id, name)

        return ResponseEntity.noContent().build()
    }

    @DeleteMapping
    fun destroy(@PathVariable id: Long, @PathVariable name: String): ResponseEntity<Unit> {
        tagManager.removePostTag(id, name)

        return ResponseEntity.noContent().build()
    }
}
