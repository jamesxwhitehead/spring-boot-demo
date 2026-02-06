package com.example.demo.service

interface TagManager {
    fun addPostTag(postId: Long, tagName: String)

    fun removePostTag(postId: Long, tagName: String)
}
