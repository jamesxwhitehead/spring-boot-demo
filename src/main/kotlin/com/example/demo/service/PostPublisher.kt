package com.example.demo.service

interface PostPublisher {
    @Throws(IllegalStateException::class)
    fun publish(id: Long)

    @Throws(IllegalStateException::class)
    fun archive(id: Long)
}
