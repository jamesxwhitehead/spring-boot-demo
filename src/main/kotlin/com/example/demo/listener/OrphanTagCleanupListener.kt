package com.example.demo.listener

import com.example.demo.event.PostTagRemovedEvent
import com.example.demo.repository.TagRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class OrphanTagCleanupListener(private val repository: TagRepository) {
    @EventListener
    @Transactional(propagation = Propagation.REQUIRED)
    fun onPostTagRemovedEvent(event: PostTagRemovedEvent) {
        repository.deleteIfOrphaned(event.tagId)
    }
}
