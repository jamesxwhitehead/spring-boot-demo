package com.example.demo.controller

import com.example.demo.entity.Post
import com.example.demo.repository.PostRepository
import com.example.demo.service.PostPublisher
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    private val repository: PostRepository,
    private val publisher: PostPublisher
) {
    @GetMapping
    fun index(): ResponseEntity<List<Post>> {
        val posts = repository.findAll()

        return ResponseEntity.ok(posts)
    }

    @PatchMapping("/{id}/publish")
    fun publish(@PathVariable id: Long): ResponseEntity<Post> {
        val post = publisher.publish(id)

        return ResponseEntity.ok(post)
    }

    @PatchMapping("/{id}/archive")
    fun archive(@PathVariable id: Long): ResponseEntity<Post> {
        val post = publisher.archive(id)

        return ResponseEntity.ok(post)
    }
}
