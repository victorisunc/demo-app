package de.mobile.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import de.mobile.dto.CustomerDto
import de.mobile.repository.MobileCustomerRepository
import de.mobile.service.AdService
import de.mobile.service.CustomerService
import de.mobile.testCustomerDto
import de.mobile.testMobileCustomer
import io.kotlintest.shouldBe
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
class CustomerResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var mobileCustomerRepository: MobileCustomerRepository

    @MockkBean
    private lateinit var adService: AdService

    @MockkBean
    lateinit var customerService: CustomerService


    @Test
    fun `get customer by id`() {
        val id = 1L

        every { mobileCustomerRepository.get(id) } returns testMobileCustomer(id)
        every { customerService.get(id) } returns testCustomerDto(id)

        mockMvc.perform(
            get("/customers/$id")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.firstName", `is`("First")))
            .andExpect(jsonPath("$.lastName", `is`("Last")))
    }
    @Test
    fun `get customers`() {

        val customers = listOf(
            testCustomerDto(1),
            testCustomerDto(2),
            testCustomerDto(3)
        )

        every { customerService.list() } returns customers

        val response = mockMvc.perform(
            get("/customers")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn().response.contentAsString
        response shouldBe mapper.writeValueAsString(customers)
    }

    @Test
    fun `create customer`() {
        val id = 1L

        every { customerService.create(any()) } returns id

        mockMvc.perform(
            post("/customers")
                .contentType(APPLICATION_JSON)
                .content(
                    mapper.writeValueAsString(testCustomerDto())
                )
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.success", `is`(true)))
            .andExpect(jsonPath("$.message", `is`("Customer with id: ${id} created successfully")))
    }

    @Test
    fun `create customer fails with invalid fields`() {
        val id = 1L

        every { customerService.create(any()) } returns id

        table(
            headers("firstName", "lastName", "email", "companyName","expectedErrors"),
            row(null, "Smith", "email@email.com", null,
                arrayOf("firstName: must not be blank")),
            row(null, null, "email@email.com", null,
                arrayOf("firstName: must not be blank", "lastName: must not be blank")),
            row(null, null, null, null,
                arrayOf("firstName: must not be blank", "lastName: must not be blank", "email: must not be blank")),
            row("John", "Smith", "invalid.email", "company name",
                arrayOf("email: Email should be valid"))
        ).forAll { firstName, lastName, email, companyName, expectedErrors ->
            mockMvc.perform(
                post("/customers")
                    .contentType(APPLICATION_JSON)
                    .content(
                        mapper.writeValueAsString(CustomerDto.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .email(email)
                            .companyName(companyName)
                            .build()
                        ))
            ).andExpect(status().isBadRequest)
                .andExpect(jsonPath("$.errors", containsInAnyOrder(*expectedErrors)))
        }

    }

    @Test
    fun `delete customer by id`() {
        val id = 1L

        every { customerService.delete(any()) } just runs

        mockMvc.perform(
            delete("/customers/${id}")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.message", `is`("Customer deleted successfully")))
    }

}
