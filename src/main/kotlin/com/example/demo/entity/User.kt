package com.example.demo.entity

import com.example.demo.dto.request.CreateUserRequestDto
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OrderBy

@Entity(name = "app_user")
class User(
    @Column(unique = true, nullable = false)
    var username: String,

    @JsonIgnore
    @Column(nullable = false)
    var password: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long? = null

    @Embedded
    val account: Account = Account()

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id")]
    )
    @Column(name = "role", nullable = false)
    @OrderBy("role ASC")
    val roles: MutableSet<Role> = mutableSetOf(Role.ROLE_USER)

    companion object {
        fun fromDto(dto: CreateUserRequestDto) = User(username = dto.username, password = dto.password)
    }
}
