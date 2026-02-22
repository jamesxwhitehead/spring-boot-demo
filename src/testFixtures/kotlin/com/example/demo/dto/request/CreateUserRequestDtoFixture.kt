package com.example.demo.dto.request

import net.datafaker.Faker

class CreateUserRequestDtoFixture {
    companion object {
        fun create(faker: Faker) = CreateUserRequestDto(
            faker.credentials().username(),
            faker.credentials().password()
        )

        fun blank() = CreateUserRequestDto("", "")

        fun nullFields() = mapOf(
            "username" to null,
            "password" to null
        )
    }
}
