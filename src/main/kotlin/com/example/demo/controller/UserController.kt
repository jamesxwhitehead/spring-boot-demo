package com.example.demo.controller

import com.example.demo.dto.request.CreateUserRequestDto
import com.example.demo.entity.User
import com.example.demo.exception.ResourceNotFoundException
import com.example.demo.exception.byId
import com.example.demo.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val repository: UserRepository) : AbstractController() {
    @GetMapping
    fun index(): ResponseEntity<List<User>> {
        val users = repository.findAll()

        return ResponseEntity.ok(users)
    }

    @PostMapping
    fun store(@Validated @RequestBody request: CreateUserRequestDto): ResponseEntity<User> {
        if (repository.existsByUsername(request.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }

        val user = User.fromDto(request)

        repository.save(user)

        val location = buildLocationHeader(user.id!!)

        return ResponseEntity.created(location).body(user)
    }

    @GetMapping("/{id}")
    fun show(@PathVariable id: Long): ResponseEntity<User> {
        val user = repository.findByIdOrNull(id) ?: throw ResourceNotFoundException.byId<User>(id)

        return ResponseEntity.ok(user)
    }
}
