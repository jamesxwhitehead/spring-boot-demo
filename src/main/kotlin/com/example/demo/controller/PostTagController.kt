package com.example.demo.controller

import com.example.demo.repository.PostRepository
import com.example.demo.repository.TagRepository
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts/{id}/tags/{name}")
class PostTagController(
    private val postRepository: PostRepository,
    private val tagRepository: TagRepository
) {
    @PutMapping
    fun update(@PathVariable id: Long, @PathVariable name: String) {
        val post = postRepository.findById(id).orElseThrow()
        val tag = tagRepository.findOrNew(name)

        post.addTag(tag)

        postRepository.save(post)
    }

    @DeleteMapping
    fun destroy(@PathVariable id: Long, @PathVariable name: String) {
        val post = postRepository.findById(id).orElseThrow()
        val tag = tagRepository.findByName(name).orElseThrow()

        post.removeTag(tag)

        postRepository.save(post)
    }
}
