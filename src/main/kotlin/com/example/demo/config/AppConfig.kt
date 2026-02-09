package com.example.demo.config

import com.example.demo.config.factory.FakerFactory
import net.datafaker.Faker
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {
    @Bean
    fun faker(): Faker = FakerFactory.create()
}
