package com.example.demo.controller

import com.example.demo.dto.request.CreatePostRequestDto
import com.example.demo.entity.Post
import com.example.demo.exception.ResourceNotFoundException
import com.example.demo.exception.byId
import com.example.demo.repository.PostRepository
import com.example.demo.service.PostPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
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
    fun store(@Validated @RequestBody request: CreatePostRequestDto): ResponseEntity<Post> {
        val post = Post.fromDto(request)

        repository.save(post)

        return ResponseEntity.created(buildLocationHeader(post.id!!)).body(post)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: Long): ResponseEntity<Post> {
        val post = repository.findByIdOrNull(id) ?: throw ResourceNotFoundException.byId<Post>(id)

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
