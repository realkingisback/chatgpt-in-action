package de.tech26.chatgptinaction.adapters.inbound.api

import io.mockk.called
import io.mockk.clearMocks
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import de.tech26.chatgptinaction.domain.model.User
import de.tech26.chatgptinaction.application.services.UserService
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

class UserControllerTest {
    private lateinit var userService: UserService
    private lateinit var userController: UserController

    @BeforeEach
    fun setUp() {
        userService = mockk()
        userController = UserController(userService)
    }

    @Test
    fun `createUser should create a user and return UserResponse with HttpStatus CREATED`() {
        val request = CreateUserRequest("John Doe", "john.doe@example.com")
        val expectedUser = User(UUID.randomUUID(), "John Doe", "john.doe@example.com")
        val expectedResponse = UserResponse(expectedUser.id, expectedUser.name, expectedUser.email)

        every { userService.createUser(any()) } returns expectedUser

        val response = userController.createUser(request)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `deleteUser should delete a user`() {
        val userId = UUID.randomUUID()

        every { userService.deleteUser(userId) } returns 1

        userController.deleteUser(userId)

        // No exception should be thrown if the operation is successful
        verify { userService.deleteUser(userId) }
    }

    @Test
    fun `updateUser should update a user and return UserResponse with HttpStatus OK`() {
        val request = UpdateUserRequest(UUID.randomUUID(), "Jane Doe")
        val updatedUser = User(request.id, request.name, "jane.doe@example.com")
        val expectedResponse = UserResponse(updatedUser.id, updatedUser.name, updatedUser.email)

        every { userService.updateUser(request.id, request.name) } returns updatedUser

        val response = userController.updateUser(request)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun `updateUser should throw ResponseStatusException with HttpStatus NOT_FOUND when user not found`() {
        val request = UpdateUserRequest(UUID.randomUUID(), "Jane Doe")

        every { userService.updateUser(request.id, request.name) } returns null

        val exception = assertThrows<ResponseStatusException> {
            userController.updateUser(request)
        }

        assertEquals(HttpStatus.NOT_FOUND, exception.statusCode)
    }
}
