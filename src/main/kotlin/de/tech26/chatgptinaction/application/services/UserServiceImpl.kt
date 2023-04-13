package de.tech26.chatgptinaction.application.services

import de.tech26.chatgptinaction.adapters.outbound.persistence.UserRepository
import de.tech26.chatgptinaction.domain.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    @Transactional
    override fun createUser(command: CreateUserCommand): User {
        val id = UUID.randomUUID()
        val user = User(
            id = id,
            name = command.name,
            email = command.email
        )
        return userRepository.createUser(user)
    }
}
