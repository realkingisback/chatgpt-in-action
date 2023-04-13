package de.tech26.chatgptinaction.domain

import de.tech26.chatgptinaction.adapters.outbound.persistence.UserRepository
import de.tech26.chatgptinaction.domain.model.User
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(private val jdbcTemplate: JdbcTemplate) : UserRepository {

    override fun createUser(user: User): User {
        val sql = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)"
        jdbcTemplate.update(
            sql,
            user.id,
            user.name,
            user.email
        )
        return user
    }
}
