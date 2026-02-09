package com.example.demo.controller

import com.example.demo.entity.Post
import com.example.demo.exception.ResourceNotFoundException
import com.example.demo.repository.PostRepository
import com.example.demo.service.PostPublisher
import jakarta.validation.Valid
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(
    private val repository: PostRepository,
    private val publisher: PostPublisher
) : AbstractController() {
    @GetMapping
    fun index(): ResponseEntity<List<Post>> {
        val posts = repository.findAll()

        return ResponseEntity.ok(posts)
    }

    @PostMapping
    fun store(@Valid @RequestBody post: Post): ResponseEntity<Post> {
        repository.save(post)

        val location = buildLocationHeader(post.id!!)

        return ResponseEntity.created(location).body(post)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: Long): ResponseEntity<Post> {
        val post = repository.findByIdOrNull(id) ?: throw ResourceNotFoundException.byId("Post", id)

        return ResponseEntity.ok(post)
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
