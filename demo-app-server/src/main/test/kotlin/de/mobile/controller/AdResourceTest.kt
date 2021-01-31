package de.mobile.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import de.mobile.dto.CreateAdRequestDto
import de.mobile.repository.MobileAdRepository
import de.mobile.service.AdService
import de.mobile.service.CustomerService
import de.mobile.testAdDto
import de.mobile.testCreateAdRequestDto
import de.mobile.testCustomerDto
import de.mobile.testMobileAd
import io.kotlintest.shouldBe
import io.kotlintest.tables.forAll
import io.kotlintest.tables.headers
import io.kotlintest.tables.row
import io.kotlintest.tables.table
import io.mockk.every
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

@WebMvcTest
class AdResourceTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @MockkBean
    private lateinit var mobileAdRepository: MobileAdRepository

    @MockkBean
    private lateinit var adService: AdService

    @MockkBean
    lateinit var customerService: CustomerService


    @Test
    fun `get ad by id`() {
        val id = 1L

        val customer = testCustomerDto(id)
        val adDto = testAdDto(id, customer = customer)
        val ad = testMobileAd(id)
        every { mobileAdRepository.get(id) } returns ad
        every { adService.get(id) } returns adDto

        mockMvc.perform(
            get("/ads/$id")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk)
            .andExpect(jsonPath("$.make", `is`(adDto.make)))
            .andExpect(jsonPath("$.model", `is`(adDto.model)))
            .andExpect(jsonPath("$.description", `is`(adDto.description)))
            .andExpect(jsonPath("$.category", `is`(adDto.category)))
            .andExpect(jsonPath("$.price", `is`(adDto.price.toInt())))
            .andExpect(jsonPath("$.customer.id", `is`(customer.id.toInt())))
    }

    @Test
    fun `get ads`() {

        val ads = listOf(
            testAdDto(1, customer = testCustomerDto(1)),
            testAdDto(2, customer = testCustomerDto(2)),
            testAdDto(3, customer = testCustomerDto(3))
        )

        every { adService.list() } returns ads

        val response = mockMvc.perform(
            get("/ads")
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn().response.contentAsString
        response shouldBe mapper.writeValueAsString(ads)
    }

    @Test
    fun `create ad`() {
        val id = 1L

        every { adService.create(any()) } returns id

        mockMvc.perform(
            post("/ads")
                .contentType(APPLICATION_JSON)
                .content(
                    mapper.writeValueAsString(testCreateAdRequestDto())
                )
        ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.success", `is`(true)))
            .andExpect(jsonPath("$.message", `is`("Ad with id: ${id} created successfully")))
    }

    @Test
    fun `create ad fails with invalid fields`() {
        val id = 1L

        every { adService.create(any()) } returns id

        table(
            headers("make", "model", "description", "category", "price", "mobileCustomerId", "expectedErrors"),
            row(
                null, "model", null, "Car", null, 1L,
                arrayOf("make: must not be blank")
            ),
            row(
                null, null, null, "Car", 100, 1L,
                arrayOf("make: must not be blank", "model: must not be blank")
            ),
            row(
                null, null, null, null, 100, 1L,
                arrayOf("make: must not be blank", "model: must not be blank", "category: must not be blank")
            ),
            row(
                null, null, null, null, 100, null,
                arrayOf(
                    "make: must not be blank",
                    "model: must not be blank",
                    "category: must not be blank",
                    "mobileCustomerId: must not be null"
                )
            )
        ).forAll { make, model, description, category, price, mobileCustomerId, expectedErrors ->
            mockMvc.perform(
                post("/ads")
                    .contentType(APPLICATION_JSON)
                    .content(
                        mapper.writeValueAsString(
                            CreateAdRequestDto.builder()
                                .make(make)
                                .model(model)
                                .description(description)
                                .category(category)
                                .price(price?.let { BigDecimal(it) })
                                .mobileCustomerId(mobileCustomerId)
                                .build()
                        )
                    )
            ).andExpect(status().isBadRequest).andExpect(jsonPath("$.errors", containsInAnyOrder(*expectedErrors)))
        }

    }

}
