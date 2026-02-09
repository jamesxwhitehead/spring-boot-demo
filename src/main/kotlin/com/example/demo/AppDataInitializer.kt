package com.example.demo

import com.example.demo.entity.Post
import com.example.demo.repository.PostRepository
import com.example.demo.repository.TagRepository
import net.datafaker.Faker
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AppDataInitializer(
    private val faker: Faker,
    private val postRepository: PostRepository,
    private val tagRepository: TagRepository
) : ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments) {
        repeat(10) {
            val post = Post(
                faker.book().author(),
                faker.book().title(),
                faker.lorem().paragraph()
            )

            repeat(2) {
                val tag = tagRepository.findOrNew(faker.book().genre())

                post.addTag(tag)
            }

            postRepository.save(post)
        }

        postRepository.flush()
    }
}
