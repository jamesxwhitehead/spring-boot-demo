package com.example.demo.security

import com.example.demo.dto.request.CreateUserRequestDto
import com.example.demo.entity.User
import com.example.demo.repository.UserRepository
import org.springframework.security.core.userdetails.User.builder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserManagerImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserManager {
    @Transactional
    override fun persist(dto: CreateUserRequestDto): User {
        val user = User.fromDto(dto).apply {
            password = passwordEncoder.encode(password)!!
        }

        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException.fromUsername(username)

        return builder()
            .username(user.username)
            .password(user.password)
            .authorities(user.roles.map { RoleAuthority(it) })
            .accountExpired(user.account.accountExpired)
            .accountLocked(user.account.accountLocked)
            .credentialsExpired(user.account.credentialsExpired)
            .disabled(user.account.disabled)
            .build()
    }
}
