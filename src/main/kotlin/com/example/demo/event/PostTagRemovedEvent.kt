package com.example.demo.event

data class PostTagRemovedEvent(
    val postId: Long,
    val tagId: Long
)
