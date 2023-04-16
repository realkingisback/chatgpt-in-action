package de.tech26.chatgptinaction

import com.fasterxml.jackson.databind.ObjectMapper
import de.tech26.chatgptinaction.domain.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@SpringBootTest
@AutoConfigureMockMvc
@SqlGroup(
    //Sql("/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    Sql("/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD),
    Sql("/clean-up.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
)
class UserIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `integration test for creating a user`() {
        val newUser = User(UUID.randomUUID(), "John Doe", "john.doe@example.com")
        val json = objectMapper.writeValueAsString(newUser)

        mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isCreated)
    }

    @Test
    fun `integration test for updating a user`() {
        val userId = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
        val newName = "Updated Name"

        mockMvc.perform(
            put("/users/$userId")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"$newName\"}")
        ).andExpect(status().isOk)
    }

    @Test
    fun `integration test for getting a user by id`() {
        val userId = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")

        val result = mockMvc.perform(
            get("/users/$userId")
        )
            .andExpect(status().isOk)
            .andReturn()

        val responseBody = result.response.contentAsString
        val user = objectMapper.readValue(responseBody, User::class.java)

        assertEquals("John Doe", user.name)
        assertEquals("john.doe@example.com", user.email)
    }

    @Test
    fun `integration test for getting all users`() {
        val result = mockMvc.perform(
            get("/users")
        )
            .andExpect(status().isOk)
            .andReturn()

        val responseBody = result.response.contentAsString
        val users = objectMapper.readValue(responseBody, Array<User>::class.java)

        assertEquals(2, users.size)
    }

    @Test
    fun `integration test for deleting a user`() {
        val userId = UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")

        mockMvc.perform(
            delete("/users/$userId")
        ).andExpect(status().isOk)
    }
}
