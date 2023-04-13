package de.tech26.chatgptinaction.adapters.outbound.persistence

import de.tech26.chatgptinaction.domain.model.User

interface UserRepository {
    fun createUser(user: User): User
}
