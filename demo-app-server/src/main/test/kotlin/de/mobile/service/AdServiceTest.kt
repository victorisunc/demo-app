package de.mobile.service

import de.mobile.AppConfig
import de.mobile.api.exception.BadRequestException
import de.mobile.entity.Category
import de.mobile.entity.MobileAd
import de.mobile.repository.MobileAdRepository
import de.mobile.testAdDto
import de.mobile.testCreateAdRequestDto
import de.mobile.testCustomerDto
import de.mobile.testMobileAd
import de.mobile.api.exception.ResourceNotFoundException
import io.kotlintest.matchers.collections.shouldContainAll
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper

class AdServiceTest {

    private var mobileAdRepository = mockk<MobileAdRepository>(relaxed = true)
    private var customerService = mockk<CustomerService>(relaxed = true)

    private var modelMapper: ModelMapper = AppConfig().modelMapper()

    private val adService = AdService(
        mobileAdRepository,
        customerService,
        modelMapper
    )

    @Test
    fun `gets ad by id returns ad`() {
        val ad = testMobileAd(1L)
        val customer = testCustomerDto(1L)
        val expectedAdDto = testAdDto(1L, customer = customer)

        every { mobileAdRepository.get(1L) } returns ad
        every { customerService.get(1L) } returns customer

        adService.get(1L) shouldBe expectedAdDto
    }

    @Test
    fun `gets ads`() {
        val ads = listOf(testMobileAd(1L), testMobileAd(2L), testMobileAd(3L))
        val customer = testCustomerDto(1L)
        val expectedAds = listOf(
            testAdDto(1L, customer = customer),
            testAdDto(2L, customer = customer),
            testAdDto(3L, customer = customer))

        every { mobileAdRepository.list() } returns ads
        every { customerService.get(1L) } returns customer

        val adList = adService.list()
        verify {
            mobileAdRepository.list()
            adList shouldContainAll expectedAds
        }
    }

    @Test
    fun `gets ad by id that doesn't exist returns ResourceNotFoundException`() {
        val adId = 99L
        every { mobileAdRepository.get(any()) } returns null

        val exception = Assertions.assertThrows(ResourceNotFoundException::class.java) {
            adService.get(adId)
        }

        exception.message shouldBe "Ad not found with id: $adId"
    }

    @Test
    fun `creates ad`() {
        val slot = slot<MobileAd>()

        val newAd = testCreateAdRequestDto()
        every { mobileAdRepository.create(capture(slot)) } returns 1L
        val newAdId = adService.create(newAd)

        verify {
            val ad = slot.captured
            mobileAdRepository.create(ad)
            newAdId shouldBe 1L
            ad.category shouldBe Category.valueOf(newAd.category)
            ad.description shouldBe newAd.description
            ad.make shouldBe newAd.make
            ad.model shouldBe newAd.model
            ad.price shouldBe newAd.price
            ad.mobileCustomerId shouldBe newAd.mobileCustomerId
        }
    }

    @Test
    fun `creates ad with bad enum value for Category should throw BadRequestException`() {
        val newAd = testCreateAdRequestDto(category = "SpaceX Launch Vehicle")

        val exception = Assertions.assertThrows(BadRequestException::class.java) {
            adService.create(newAd)
        }

        exception.message shouldBe "Category is invalid. Valid values are: ${Category.values().map{ it.name }}"
    }

    @Test
    fun `creates ad with invalid customer id should throw BadRequestException`() {
        val newAd = testCreateAdRequestDto()
        every { customerService.get(any()) } returns null

        val exception = Assertions.assertThrows(BadRequestException::class.java) {
            adService.create(newAd)
        }

        exception.message shouldBe "Ad is not assigned to a valid customer id: ${newAd.mobileCustomerId}"
    }


}