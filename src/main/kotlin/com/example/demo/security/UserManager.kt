package com.example.demo.security

import com.example.demo.dto.request.CreateUserRequestDto
import com.example.demo.entity.User
import org.springframework.security.core.userdetails.UserDetailsService

interface UserManager : UserDetailsService {
    fun persist(dto: CreateUserRequestDto): User
}
