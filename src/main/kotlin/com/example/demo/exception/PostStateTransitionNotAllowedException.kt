package com.example.demo.exception

class PostStateTransitionNotAllowedException : IllegalStateException {
    constructor(message: String, cause: Throwable) : super(message, cause)
}
