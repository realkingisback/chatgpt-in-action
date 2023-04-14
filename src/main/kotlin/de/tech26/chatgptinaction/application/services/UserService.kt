package de.tech26.chatgptinaction.application.services

import de.tech26.chatgptinaction.domain.model.User
import java.util.UUID

interface UserService {
    fun createUser(command: CreateUserCommand): User
    fun updateUser(id: UUID, name: String): User?
    fun deleteUser(id: UUID): Int
}

data class CreateUserCommand(
    val name: String,
    val email: String
)
