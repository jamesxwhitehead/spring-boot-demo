package com.example.demo.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Account {
    @Column(nullable = false)
    var accountExpired: Boolean = false

    @Column(nullable = false)
    var accountLocked: Boolean = false

    @Column(nullable = false)
    var credentialsExpired: Boolean = false

    @Column(nullable = false)
    var disabled: Boolean = false
}
