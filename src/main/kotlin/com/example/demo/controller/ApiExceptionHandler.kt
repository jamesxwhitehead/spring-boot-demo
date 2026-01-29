package com.example.demo.controller

import org.springframework.http.HttpStatus
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(exception: IllegalStateException): ErrorResponse {
        return ResponseStatusException(
            HttpStatus.UNPROCESSABLE_CONTENT,
            exception.message,
            exception
        )
    }
}
