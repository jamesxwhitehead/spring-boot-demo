package com.example.demo.entity

import net.datafaker.Faker

class PostFixture {
    companion object {
        fun new(faker: Faker) = Post(
            author = faker.programmingLanguage().creator(),
            title = faker.computer().linux(),
            content = faker.lorem().paragraph()
        )

        fun withTag(faker: Faker) = new(faker).apply {
            addTag(TagFixture.new(faker))
        }
    }
}
