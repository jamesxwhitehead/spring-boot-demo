package com.example.demo.dto.request

import net.datafaker.Faker

class CreatePostRequestDtoFixture {
    companion object {
        fun create(faker: Faker) = CreatePostRequestDto(
            faker.book().author(),
            faker.book().title(),
            faker.lorem().paragraph()
        )

        fun blank() = CreatePostRequestDto("", "", "")

        fun nullFields() = mapOf(
            "author" to null,
            "title" to null,
            "content" to null
        )
    }
}
