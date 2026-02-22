package com.example.demo

import com.example.demo.entity.Post
import com.example.demo.entity.Role
import com.example.demo.entity.User
import com.example.demo.repository.PostRepository
import com.example.demo.repository.TagRepository
import com.example.demo.repository.UserRepository
import net.datafaker.Faker
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AppDataInitializer(
    private val faker: Faker,
    private val postRepository: PostRepository,
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) : ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments) {
        val admin = User(
            username = "admin",
            password = $$"$2a$10$Y0MK.Yye4lEbpSkyIZ1wsu5cJbURFBG0z/YI5WJVHt4/SVUvb2pZm"
        ).apply { roles.add(Role.ROLE_ADMIN) }

        userRepository.save(admin)

        repeat(5) {
            val user = User(
                username = faker.credentials().username(),
                password = faker.credentials().password()
            )

            userRepository.save(user)
        }

        userRepository.flush()

        repeat(5) {
            val post = Post(
                author = faker.programmingLanguage().creator(),
                title = faker.computer().linux(),
                content = faker.lorem().paragraph()
            )

            post.addTag(tagRepository.findOrCreate("Linux"))
            post.addTag(tagRepository.findOrCreate(faker.computer().type()))
            post.addTag(tagRepository.findOrCreate(faker.computer().brand()))
            post.addTag(tagRepository.findOrCreate(faker.hacker().adjective()))
            post.addTag(tagRepository.findOrCreate(faker.hacker().noun()))
            post.addTag(tagRepository.findOrCreate(faker.hacker().verb()))

            postRepository.save(post)
        }

        repeat(5) {
            val post = Post(
                author = faker.programmingLanguage().creator(),
                title = faker.computer().macos(),
                content = faker.lorem().paragraph()
            )

            post.addTag(tagRepository.findOrCreate("macOS"))
            post.addTag(tagRepository.findOrCreate(faker.computer().type()))
            post.addTag(tagRepository.findOrCreate("Apple"))
            post.addTag(tagRepository.findOrCreate(faker.hacker().adjective()))
            post.addTag(tagRepository.findOrCreate(faker.hacker().noun()))
            post.addTag(tagRepository.findOrCreate(faker.hacker().verb()))

            postRepository.save(post)
        }

        repeat(5) {
            val post = Post(
                author = faker.programmingLanguage().creator(),
                title = faker.computer().windows(),
                content = faker.lorem().paragraph()
            )

            post.addTag(tagRepository.findOrCreate("Windows"))
            post.addTag(tagRepository.findOrCreate(faker.computer().type()))
            post.addTag(tagRepository.findOrCreate(faker.computer().brand()))
            post.addTag(tagRepository.findOrCreate(faker.hacker().adjective()))
            post.addTag(tagRepository.findOrCreate(faker.hacker().noun()))
            post.addTag(tagRepository.findOrCreate(faker.hacker().verb()))

            postRepository.save(post)
        }

        postRepository.flush()
    }
}
