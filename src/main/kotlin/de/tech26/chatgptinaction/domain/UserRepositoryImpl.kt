package de.tech26.chatgptinaction.domain

import de.tech26.chatgptinaction.adapters.outbound.persistence.UserRepository
import de.tech26.chatgptinaction.domain.model.User
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryJooq(private val dslContext: DSLContext) : UserRepository {

    override fun createUser(user: User): User {
//        val result = dslContext.insertInto(USER)
//            .set(USER.ID, user.id)
//            .set(USER.NAME, user.name)
//            .set(USER.EMAIL, user.email)
//            .execute()

        return user
    }
}
