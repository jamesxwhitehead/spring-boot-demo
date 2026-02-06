package com.example.demo.controller

import com.example.demo.exception.PostStateTransitionNotAllowedException
import com.example.demo.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(PostStateTransitionNotAllowedException::class)
    fun handlePostStateTransitionNotAllowedException(exception: PostStateTransitionNotAllowedException): ErrorResponse {
        return ResponseStatusException(
            HttpStatus.UNPROCESSABLE_CONTENT,
            exception.message,
            exception
        )
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(exception: ResourceNotFoundException): ErrorResponse {
        return ResponseStatusException(
            HttpStatus.NOT_FOUND,
            exception.message,
            exception
        )
    }
}
