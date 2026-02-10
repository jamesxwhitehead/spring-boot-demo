package com.example.demo.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreatePostRequestDto(
    @NotBlank
    @Size(max = 255)
    val author: String,

    @NotBlank
    @Size(max = 255)
    val title: String,

    @NotBlank
    val content: String
)
