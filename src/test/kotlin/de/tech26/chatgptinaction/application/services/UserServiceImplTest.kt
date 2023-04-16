package de.tech26.chatgptinaction.application.services

import de.tech26.chatgptinaction.adapters.outbound.persistence.UserRepository
import de.tech26.chatgptinaction.domain.model.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class UserServiceImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserServiceImpl

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        userService = UserServiceImpl(userRepository)
    }

    @Test
    fun `createUser should create a user and return the created user`() {
        val command = CreateUserCommand("John Doe", "john.doe@example.com")
        val user = User(UUID.randomUUID(), command.name, command.email)

        every { userRepository.createUser(any()) } returns user

        val createdUser = userService.createUser(command)

        assertEquals(user, createdUser)
        verify { userRepository.createUser(any()) }
    }

    @Test
    fun `updateUser should update a user and return the updated user`() {
        val id = UUID.randomUUID()
        val newName = "Jane Doe"
        val updatedUser = User(id, newName, "jane.doe@example.com")

        every { userRepository.updateUser(id, newName) } returns updatedUser

        val result = userService.updateUser(id, newName)

        assertEquals(updatedUser, result)
        verify { userRepository.updateUser(id, newName) }
    }

    @Test
    fun `deleteUser should delete a user and return the number of rows affected`() {
        val id = UUID.randomUUID()

        every { userRepository.deleteUser(id) } returns 1

        val rowsAffected = userService.deleteUser(id)

        assertEquals(1, rowsAffected)
        verify { userRepository.deleteUser(id) }
    }

    @Test
    fun `getUserById should return a user by id`() {
        val id = UUID.randomUUID()
        val user = User(id, "John Doe", "john.doe@example.com")

        every { userRepository.getUserById(id) } returns user

        val result = userService.getUserById(id)

        assertEquals(user, result)
        verify { userRepository.getUserById(id) }
    }

    @Test
    fun `getAllUsers should return a list of all users`() {
        val users = listOf(
            User(UUID.randomUUID(), "John Doe", "john.doe@example.com"),
            User(UUID.randomUUID(), "Jane Doe", "jane.doe@example.com")
        )

        every { userRepository.getAllUsers() } returns users

        val result = userService.getAllUsers()

        assertEquals(users, result)
        verify { userRepository.getAllUsers() }
    }
}
