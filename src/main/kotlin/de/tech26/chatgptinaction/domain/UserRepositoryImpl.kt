package de.tech26.chatgptinaction.domain

import de.tech26.chatgptinaction.adapters.outbound.persistence.UserRepository
import de.tech26.chatgptinaction.domain.model.User
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.sql.ResultSet
import java.sql.Types
import java.util.UUID

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
    override fun updateUser(id: UUID, name: String): User? {
        val query = "UPDATE users SET name = ? WHERE id = ? RETURNING id, name, email"
        val params = arrayOf(name, id)
        val types = intArrayOf(Types.VARCHAR, Types.OTHER)

        return try {
            jdbcTemplate.query(query, params, types) { rs, _ ->
                mapResultSetToUser(rs)
            }.firstOrNull()
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }

    override fun deleteUser(id: UUID): Int {
        val sql = "DELETE FROM users WHERE id = ?"
        return jdbcTemplate.update(sql, id)
    }

    private fun mapResultSetToUser(rs: ResultSet): User {
        return User(
            id = rs.getObject("id", UUID::class.java),
            name = rs.getString("name"),
            email = rs.getString("email")
        )
    }
}
