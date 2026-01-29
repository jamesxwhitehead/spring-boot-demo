package com.example.demo.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Version
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Entity
@OptIn(ExperimentalTime::class)
class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null

    @Version
    var version: Long? = null
        protected set

    @Column(nullable = false)
    var author: String? = null

    @Column(nullable = false)
    var title: String? = null

    @Lob
    @Column(nullable = false)
    var content: String? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: PostStatus = PostStatus.DRAFT
        protected set

    @Column
    var publishedAt: Instant? = null
        protected set

    fun publish() {
        check(status.canTransitionTo(PostStatus.PUBLISHED))

        status = PostStatus.PUBLISHED
        publishedAt = Clock.System.now()
    }

    fun archive() {
        check(status.canTransitionTo(PostStatus.ARCHIVED))

        status = PostStatus.ARCHIVED
    }
}
