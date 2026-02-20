package com.example.demo.exception

import kotlin.reflect.KClass

class ResourceNotFoundException : NoSuchElementException {
    constructor(message: String) : super(message)

    companion object {
        fun <T : Any> byId(resource: KClass<T>, id: Long) =
            byId(resourceName(resource), id)

        fun byId(resource: String, id: Long) =
            ResourceNotFoundException(buildMessage(resource, mapOf("id" to id)))

        fun <T : Any> byField(resource: KClass<T>, field: String, value: Any) =
            byField(resourceName(resource), field, value)

        fun byField(resource: String, field: String, value: Any) =
            ResourceNotFoundException(buildMessage(resource, mapOf(field to value)))

        private fun <T : Any> resourceName(resource: KClass<T>): String {
            return resource.simpleName ?: "Resource"
        }

        private fun buildMessage(resource: String, lookup: Map<String, Any>): String {
            return "$resource not found (${lookup.entries.joinToString { "${it.key}=${it.value}" }})"
        }
    }
}
