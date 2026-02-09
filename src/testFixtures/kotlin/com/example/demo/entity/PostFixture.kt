package com.example.demo.entity

import net.datafaker.Faker

class PostFixture {
    companion object {
        fun create(faker: Faker) = Post(
            faker.book().author(),
            faker.book().title(),
            faker.lorem().paragraph()
        )

        fun nullFields() = mapOf(
            "author" to null,
            "title" to null,
            "content" to null
        )

        fun blank() = Post("", "", "")
    }
}
