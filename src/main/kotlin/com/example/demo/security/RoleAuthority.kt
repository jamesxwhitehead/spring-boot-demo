package com.example.demo.security

import com.example.demo.entity.Role
import org.springframework.security.core.GrantedAuthority

class RoleAuthority(private val role: Role) : GrantedAuthority {
    override fun getAuthority(): String {
        return role.name
    }
}
