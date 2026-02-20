package com.example.demo.exception

inline fun <reified T : Any> ResourceNotFoundException.Companion.byId(id: Long) =
    byId(T::class, id)

inline fun <reified T : Any> ResourceNotFoundException.Companion.byField(field: String, value: Any) =
    byField(T::class, field, value)
