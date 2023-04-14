package de.tech26.chatgptinaction.adapters.inbound.api

import de.tech26.chatgptinaction.application.services.CreateUserCommand
import de.tech26.chatgptinaction.application.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody request: CreateUserRequest): UserResponse {
        val command = CreateUserCommand(request.name, request.email)
        val user = userService.createUser(command)
        return UserResponse(user.id, user.name, user.email)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: UUID) {
        userService.deleteUser(id)
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    fun updateUser(@RequestBody request: UpdateUserRequest) =
        userService.updateUser(request.id, request.name)?.let {
            UserResponse(it.id, it.name, it.email)
        } ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: ${request.id}")
}

data class CreateUserRequest(
    val name: String,
    val email: String
)

data class UserResponse(
    val id: UUID,
    val name: String,
    val email: String
)

data class UpdateUserRequest(val id: UUID, val name: String)
