package com.example.demo.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToMany
import org.hibernate.annotations.NaturalId

@Entity
class Tag(
    @NaturalId
    @Column(unique = true, nullable = false)
    val name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null

    @ManyToMany(mappedBy = "_tags")
    private val _posts: MutableSet<Post> = mutableSetOf()

    val posts: Set<Post>
        @JsonIgnore
        get() = _posts

    internal fun addPost(post: Post) {
        _posts.add(post)
    }

    internal fun removePost(post: Post) {
        _posts.remove(post)
    }
}
