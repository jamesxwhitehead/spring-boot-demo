package com.example.demo.controller

import com.example.demo.entity.Post
import com.example.demo.repository.PostRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(private val postRepository: PostRepository) {
    @GetMapping
    fun index(): ResponseEntity<List<Post>> {
        val posts = postRepository.findAll()

        return ResponseEntity.ok(posts)
    }
}
