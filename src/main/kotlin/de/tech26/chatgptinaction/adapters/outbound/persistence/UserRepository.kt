package de.tech26.chatgptinaction.adapters.outbound.persistence

import de.tech26.chatgptinaction.domain.model.User
import java.util.UUID

interface UserRepository {
    fun createUser(user: User): User
    fun updateUser(id: UUID, name: String): User?
    fun deleteUser(id: UUID): Int
    fun getUserById(id: UUID): User?
    fun getAllUsers(): List<User>
}
