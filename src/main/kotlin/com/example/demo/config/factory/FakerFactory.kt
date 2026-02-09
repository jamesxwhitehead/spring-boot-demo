package com.example.demo.config.factory

import net.datafaker.Faker
import java.util.Locale
import java.util.Random

class FakerFactory {
    companion object {
        @JvmStatic
        fun create(): Faker = Faker(
            Locale.of(LOCALE_LANGUAGE, LOCALE_COUNTRY),
            Random(SEED)
        )

        private const val LOCALE_LANGUAGE = "en-AU"
        private const val LOCALE_COUNTRY = "Australia"
        private const val SEED = 1L
    }
}
