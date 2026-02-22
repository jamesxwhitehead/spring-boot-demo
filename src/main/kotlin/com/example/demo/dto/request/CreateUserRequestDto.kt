package com.example.demo.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequestDto(
    @NotBlank
    @Size(max = 255)
    val username: String,

    @NotBlank
    @Size(max = 255)
    val password: String
)
