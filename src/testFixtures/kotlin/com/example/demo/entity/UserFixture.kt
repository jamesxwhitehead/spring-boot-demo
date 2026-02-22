package com.example.demo.entity

import net.datafaker.Faker

class UserFixture {
    companion object {
        fun create(faker: Faker) = User(
            faker.credentials().username(),
            faker.credentials().password()
        )

        fun disabled(faker: Faker) = create(faker).apply {
            account.disabled = true
        }
    }
}
