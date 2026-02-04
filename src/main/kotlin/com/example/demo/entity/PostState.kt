package com.example.demo.entity

enum class PostState {
    DRAFT,
    PUBLISHED,
    ARCHIVED;

    fun canTransitionTo(target: PostState): Boolean {
        return transitions.getValue(this).contains(target)
    }

    companion object {
        private val transitions: Map<PostState, Set<PostState>> = mapOf(
            DRAFT to setOf(PUBLISHED, ARCHIVED),
            PUBLISHED to setOf(ARCHIVED),
            ARCHIVED to emptySet()
        )
    }
}
