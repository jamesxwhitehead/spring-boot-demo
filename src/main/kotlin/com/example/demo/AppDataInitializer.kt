package com.example.demo

import com.example.demo.entity.Post
import jakarta.persistence.EntityManager
import net.datafaker.Faker
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
class AppDataInitializer(private val entityManager: EntityManager) : ApplicationRunner {
    private val faker = Faker(
        Locale.of(LOCALE_LANGUAGE, LOCALE_COUNTRY),
        Random(SEED)
    )

    @Transactional
    override fun run(args: ApplicationArguments) {
        (1..10).forEach { _ ->
            val post = Post().apply {
                author = faker.book().author()
                title = faker.book().title()
                content = faker.lorem().paragraph()
            }

            entityManager.persist(post)
        }

        entityManager.flush()
    }

    companion object {
        private const val LOCALE_LANGUAGE = "en-AU"
        private const val LOCALE_COUNTRY = "Australia"
        private const val SEED = 1L
    }
}
