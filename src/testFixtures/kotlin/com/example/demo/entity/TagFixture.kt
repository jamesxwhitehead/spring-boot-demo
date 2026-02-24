package com.example.demo.entity

import net.datafaker.Faker

class TagFixture {
    companion object {
        fun new(faker: Faker) = Tag(faker.unique().fetchFromYaml("hacker.ingverb"))
    }
}
