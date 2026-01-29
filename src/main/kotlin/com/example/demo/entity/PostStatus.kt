package com.example.demo.entity

enum class PostStatus {
    DRAFT,
    PUBLISHED,
    ARCHIVED;

    fun canTransitionTo(target: PostStatus): Boolean {
        return transitions.getValue(this).contains(target)
    }

    companion object {
        private val transitions: Map<PostStatus, Set<PostStatus>> = mapOf(
            DRAFT to setOf(PUBLISHED, ARCHIVED),
            PUBLISHED to setOf(ARCHIVED),
            ARCHIVED to emptySet()
        )
    }
}
