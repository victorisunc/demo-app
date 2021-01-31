package de.mobile

import de.mobile.dto.AdDto
import de.mobile.dto.CreateAdRequestDto
import de.mobile.dto.CustomerDto
import de.mobile.entity.Category
import de.mobile.entity.MobileAd
import de.mobile.entity.MobileCustomer
import java.math.BigDecimal


fun testMobileCustomer(
    id: Long? = null,
    firstName: String = "First",
    lastName: String = "Last",
    companyName: String = "Company",
    email: String = "aaa@aaa.com"
): MobileCustomer = MobileCustomer.builder()
    .id(id)
    .firstName(firstName)
    .lastName(lastName)
    .companyName(companyName)
    .email(email)
    .build()

fun testCustomerDto(
    id: Long? = null,
    firstName: String = "First",
    lastName: String = "Last",
    companyName: String = "Company",
    email: String = "aaa@aaa.com"
): CustomerDto = CustomerDto.builder()
    .id(id)
    .firstName(firstName)
    .lastName(lastName)
    .companyName(companyName)
    .email(email)
    .build()

fun testMobileAd(
    id: Long? = null,
    make: String = "Make",
    model: String = "Model",
    category: Category = Category.Car,
    description: String = "Description",
    price: BigDecimal = BigDecimal(10),
    mobileCustomerId: Long = 1L
): MobileAd = MobileAd.builder()
    .id(id)
    .make(make)
    .model(model)
    .category(category)
    .description(description)
    .price(price)
    .mobileCustomerId(mobileCustomerId)
    .build()

fun testAdDto(
    id: Long? = null,
    make: String = "Make",
    model: String = "Model",
    category: Category = Category.Car,
    description: String = "Description",
    price: BigDecimal = BigDecimal(10),
    customer: CustomerDto
): AdDto = AdDto.builder()
    .id(id)
    .make(make)
    .model(model)
    .category(category.name)
    .description(description)
    .price(price)
    .customer(customer)
    .build()

fun testCreateAdRequestDto(
    make: String = "Make",
    model: String = "Model",
    category: String = "Car",
    description: String = "Description",
    price: BigDecimal = BigDecimal(10),
    mobileCustomerId: Long = 1L
): CreateAdRequestDto = CreateAdRequestDto.builder()
    .make(make)
    .model(model)
    .category(category)
    .description(description)
    .price(price)
    .mobileCustomerId(mobileCustomerId)
    .build()
