package com.example.demo.security

import com.example.demo.entity.Role
import com.example.demo.entity.User
import com.example.demo.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class UserManagerImplTest {
    private val userRepository = mock<UserRepository>()
    private val passwordEncoder = mock<PasswordEncoder>()
    private val userManager = UserManagerImpl(userRepository, passwordEncoder)

    @Test
    fun loadUserByUsername() {
        val user = User(username = "test", password = "password")
        user.roles.add(Role.ROLE_ADMIN)
        given(userRepository.findByUsername("test")).willReturn(user)

        val userDetails = userManager.loadUserByUsername("test")

        assertThat(userDetails.username).isEqualTo(user.username)
        assertThat(userDetails.password).isEqualTo(user.password)
        assertThat(userDetails.authorities).flatExtracting({ it.authority }).containsOnly(Role.ROLE_USER.name, Role.ROLE_ADMIN.name)
        assertThat(userDetails.isAccountNonExpired).isTrue()
        assertThat(userDetails.isAccountNonLocked).isTrue()
        assertThat(userDetails.isCredentialsNonExpired).isTrue()
        assertThat(userDetails.isEnabled).isTrue()
    }

    @Test
    fun loadUserByUsernameShouldThrowUsernameNotFoundExceptionWhenUserDoesNotExist() {
        given(userRepository.findByUsername("test")).willReturn(null)

        assertThrows<UsernameNotFoundException> { userManager.loadUserByUsername("test") }
    }
}
