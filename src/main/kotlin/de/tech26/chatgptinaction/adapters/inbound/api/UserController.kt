package de.tech26.chatgptinaction.adapters.inbound.api

import de.tech26.chatgptinaction.application.services.CreateUserCommand
import de.tech26.chatgptinaction.application.services.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
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
