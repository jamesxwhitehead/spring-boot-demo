package com.example.demo.service

import com.example.demo.entity.Post
import com.example.demo.exception.PostStateTransitionNotAllowedException

interface PostPublisher {
    @Throws(PostStateTransitionNotAllowedException::class)
    fun publish(id: Long): Post

    @Throws(PostStateTransitionNotAllowedException::class)
    fun archive(id: Long): Post
}
