package com.example.demo.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.Lob
import jakarta.persistence.ManyToMany
import jakarta.persistence.Version
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Entity
@OptIn(ExperimentalTime::class)
class Post(
    @Column(nullable = false)
    var author: String,

    @Column(nullable = false)
    var title: String,

    @Lob
    @Column(nullable = false)
    var content: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null

    @Version
    var version: Int? = null
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var state: PostState = PostState.DRAFT
        protected set

    @Column
    var publishedAt: Instant? = null
        protected set

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.REMOVE])
    @JoinTable(
        name = "post_tag",
        joinColumns = [JoinColumn(name = "post_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    private val _tags: MutableSet<Tag> = mutableSetOf()

    val tags: Set<Tag>
        get() = _tags

    fun addTag(tag: Tag) {
        _tags.add(tag)
        tag.addPost(this)
    }

    fun removeTag(tag: Tag) {
        _tags.remove(tag)
        tag.removePost(this)
    }

    @Throws(IllegalStateException::class)
    fun publish() {
        check(state.canTransitionTo(PostState.PUBLISHED))

        state = PostState.PUBLISHED
        publishedAt = Clock.System.now()
    }

    @Throws(IllegalStateException::class)
    fun archive() {
        check(state.canTransitionTo(PostState.ARCHIVED))

        state = PostState.ARCHIVED
    }
}
