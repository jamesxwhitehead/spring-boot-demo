package com.example.demo.service

import com.example.demo.entity.Post
import com.example.demo.entity.Tag
import com.example.demo.event.PostTagRemovedEvent
import com.example.demo.exception.ResourceNotFoundException
import com.example.demo.exception.byField
import com.example.demo.exception.byId
import com.example.demo.repository.PostRepository
import com.example.demo.repository.TagRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagManagerImpl(
    private val postRepository: PostRepository,
    private val tagRepository: TagRepository,
    private val eventPublisher: ApplicationEventPublisher
) : TagManager {
    @Transactional
    override fun addPostTag(postId: Long, tagName: String) {
        val post = postRepository.findByIdOrNull(postId) ?: throw ResourceNotFoundException.byId<Post>(postId)
        val tag = tagRepository.findOrCreate(tagName)

        post.addTag(tag)

        postRepository.save(post)
    }

    @Transactional
    override fun removePostTag(postId: Long, tagName: String) {
        val post = postRepository.findByIdOrNull(postId) ?: throw ResourceNotFoundException.byId<Post>(postId)
        val tag = tagRepository.findByName(tagName) ?: throw ResourceNotFoundException.byField<Tag>("name", tagName)

        post.removeTag(tag)

        postRepository.save(post)

        val event = PostTagRemovedEvent(postId, tag.id!!)

        eventPublisher.publishEvent(event)
    }
}
