package com.example.demo.entity

import net.datafaker.Faker

class UserFixture {
    companion object {
        fun new(faker: Faker) = User(
            faker.credentials().username(),
            faker.credentials().password()
        )

        fun disabled(faker: Faker) = new(faker).apply {
            account.disabled = true
        }
    }
}
