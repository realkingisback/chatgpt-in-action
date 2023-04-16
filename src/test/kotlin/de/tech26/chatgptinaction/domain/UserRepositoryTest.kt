package de.tech26.chatgptinaction.domain

import de.tech26.chatgptinaction.adapters.outbound.persistence.UserRepository
import de.tech26.chatgptinaction.domain.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.UUID

@ExtendWith(SpringExtension::class)
@JdbcTest
@Import(UserRepositoryImpl::class)
class UserRepositoryTest {

    @Autowired
    private lateinit var userRepository: UserRepositoryImpl

    @Test
    fun `createUser should create a user and return the created user`() {
        val user = User(UUID.randomUUID(), "John Doe", "john.doe@example.com")

        val createdUser = userRepository.createUser(user)

        assertEquals(user, createdUser)
    }

    @Test
    fun `updateUser should update a user and return the updated user`() {
        // given
        val id = UUID.randomUUID()
        val name = "Jane Doe"
        val email = "jane.doe@example.com"
        val user = User(id, name, email)

        userRepository.createUser(user)

        // when
        val newName = "Updated Jane Doe"
        val updatedUser = userRepository.updateUser(id, newName)

        // then
        assertEquals(newName, updatedUser?.name)
    }

    @Test
    fun `updateUser should return null if the user is not found`() {
        val id = UUID.randomUUID()
        val name = "Jane Doe"

        val updatedUser = userRepository.updateUser(id, name)

        assertNull(updatedUser)
    }

    @Test
    fun `deleteUser should delete a user and return the number of rows affected`() {
        val id = UUID.randomUUID()
        val user = User(id, "John Doe", "john.doe@example.com")

        userRepository.createUser(user)

        val rowsAffected = userRepository.deleteUser(id)

        assertEquals(1, rowsAffected)
    }
}
