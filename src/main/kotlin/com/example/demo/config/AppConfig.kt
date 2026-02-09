package com.example.demo.config

import net.datafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Locale
import java.util.Random

@Configuration
class AppConfig {
    @Bean
    fun faker(): Faker = Faker(
        Locale.of(LOCALE_LANGUAGE, LOCALE_COUNTRY),
        Random(SEED)
    )

    private companion object {
        const val LOCALE_LANGUAGE = "en-AU"
        const val LOCALE_COUNTRY = "Australia"
        const val SEED = 1L
    }
}
