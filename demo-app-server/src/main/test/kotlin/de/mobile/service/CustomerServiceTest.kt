package de.mobile.service

import de.mobile.AppConfig
import de.mobile.api.exception.BadRequestException
import de.mobile.entity.MobileCustomer
import de.mobile.repository.MobileCustomerRepository
import de.mobile.testMobileCustomer
import de.mobile.testCustomerDto
import de.mobile.api.exception.ResourceNotFoundException
import io.kotlintest.matchers.collections.shouldHaveSize
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper

class CustomerServiceTest {

    private var mobileCustomerRepository = mockk<MobileCustomerRepository>(relaxed = true)

    private var modelMapper: ModelMapper = AppConfig().modelMapper()

    private val customerService = CustomerService(
        mobileCustomerRepository,
        modelMapper
    )

    @Test
    fun `gets customer by id returns customer`() {
        val customer = testMobileCustomer(1L)
        val expectedCustomerDto = testCustomerDto(1L)
        every { mobileCustomerRepository.get(1L) } returns customer

        customerService.get(1L) shouldBe expectedCustomerDto
    }

    @Test
    fun `gets customers`() {
        val customers = listOf(testMobileCustomer(1L), testMobileCustomer(2L), testMobileCustomer(3L))

        every { mobileCustomerRepository.list() } returns customers
        val customerList = customerService.list()

        customerList shouldHaveSize customers.size
    }

    @Test
    fun `gets customer by id that doesn't exist returns ResourceNotFoundException`() {
        val customerId = 99L
        every { mobileCustomerRepository.get(any()) } returns null

        val exception = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            customerService.get(customerId)
        }

        exception.message shouldBe "Customer not found with id: $customerId"
    }

    @Test
    fun `creates customer`() {
        val slot = slot<MobileCustomer>()
        every { mobileCustomerRepository.create(capture(slot)) } returns 1L

        val newCustomer = testCustomerDto(email = "email@email.com")
        val newCustomerId = customerService.create(newCustomer)

        verify {
            val customer = slot.captured
            mobileCustomerRepository.create(customer)
            newCustomerId shouldBe 1L
            customer.firstName shouldBe newCustomer.firstName
            customer.lastName shouldBe newCustomer.lastName
            customer.companyName shouldBe newCustomer.companyName
            customer.email shouldBe newCustomer.email
        }
    }

    @Test
    fun `deletes customer`() {
        val slot = slot<Long>()
        every { mobileCustomerRepository.delete(capture(slot)) } returns true

        val customerToDelete = testCustomerDto(1L, email = "email@email.com")
        customerService.delete(customerToDelete.id)

        verify {
            val customerId = slot.captured
            mobileCustomerRepository.delete(customerToDelete.id)
            customerId shouldBe customerToDelete.id
        }
    }

    @Test
    fun `deletes customer with non-existing customer id returns BadRequestException`() {
        val customerToDelete = testCustomerDto(9999L, email = "email@email.com")
        every { mobileCustomerRepository.delete(any()) } returns false

        val exception = Assertions.assertThrows(BadRequestException::class.java) {
            customerService.delete(customerToDelete.id)
        }

        verify {
            mobileCustomerRepository.delete(customerToDelete.id) shouldBe false
            exception.message shouldBe "Could not delete Customer with id: ${customerToDelete.id}"
        }
    }
}