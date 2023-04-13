package de.tech26.chatgptinaction.application.services

import de.tech26.chatgptinaction.domain.model.User

interface UserService {
    fun createUser(command: CreateUserCommand): User
}

data class CreateUserCommand(
    val name: String,
    val email: String
)
