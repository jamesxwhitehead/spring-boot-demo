package com.example.demo.exception

class ResourceNotFoundException : NoSuchElementException {
    constructor(message: String) : super(message)

    companion object {
        fun byId(resource: String, id: Long) =
            ResourceNotFoundException(buildMessage(resource, mapOf("id" to id)))

        fun byField(resource: String, field: String, value: Any) =
            ResourceNotFoundException(buildMessage(resource, mapOf(field to value)))

        private fun buildMessage(resource: String, lookup: Map<String, Any>): String {
            return "$resource not found (${lookup.entries.joinToString { "${it.key}=${it.value}" }})"
        }
    }
}
