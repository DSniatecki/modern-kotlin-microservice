package com.dsniatecki.carstorage.car

import com.dsniatecki.carstorage.CarStorageApplication
import com.dsniatecki.carstorage.cleanDb
import com.dsniatecki.carstorage.createDbTestContainer
import com.dsniatecki.carstorage.registerDbProperties
import com.dsniatecki.carstorage.toList
import com.dsniatecki.carstorage.utils.generateId
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(classes = [CarStorageApplication::class])
@Testcontainers
internal class CarServiceTest(
    @Autowired private val carService: CarService,
    @Autowired private val databaseClient: DatabaseClient
) {
    companion object {
        @Container
        private val dbContainer = createDbTestContainer()

        @DynamicPropertySource
        @JvmStatic
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registerDbProperties(dbContainer, registry)
        }
    }

    @AfterEach
    fun clean() {
        cleanDb(databaseClient)
    }

    @Test
    fun `Should not get car when db is empty`() {
        assertThat(carService.get(generateId()).block()).isNull()
    }

    @Test
    fun `Should not get car when car with given id doesn't exist`() {
        carService.save(createTestCarData(brand = "Brand1")).block()!!
        carService.save(createTestCarData(brand = "Brand2")).block()!!
        assertThat(carService.get(generateId()).block()).isNull()
    }

    @Test
    fun `Should get car`() {
        val savedCar = carService.save(createTestCarData()).block()!!
        assertThat(carService.get(savedCar.id).block()).isEqualTo(savedCar)
    }

    @Test
    fun `Should get multiple cars`() {
        val savedCar1 = carService.save(createTestCarData(brand = "Brand1")).block()!!
        val savedCar2 = carService.save(createTestCarData(brand = "Brand2")).block()!!
        assertThat(carService.getMultiple(setOf(savedCar1.id, savedCar2.id)).toList())
            .isEqualTo(listOf(savedCar1, savedCar2))
    }

    @Test
    fun `Should get all cars`() {
        val savedCar1 = carService.save(createTestCarData(brand = "Brand1")).block()!!
        val savedCar2 = carService.save(createTestCarData(brand = "Brand2")).block()!!
        assertThat(carService.getAll().toList()).isEqualTo(listOf(savedCar1, savedCar2))
    }

    @Test
    fun `Should update car`() {
        val savedCar = carService.save(createTestCarData()).block()!!
        val updateData = CarData(
            brand = "UpdatedBrand",
            model = "UpdatedModel",
            producedAt = savedCar.producedAt.plusYears(1)
        )
        val updatedCar = carService.update(savedCar.id, updateData).block()!!
        assertThat(updatedCar).isEqualTo(savedCar.copy(
            brand = updateData.brand,
            model = updateData.model,
            producedAt = updatedCar.producedAt,
            createdAt = savedCar.createdAt,
            updatedAt = updatedCar.updatedAt
        ))
        assertThat(updatedCar.updatedAt).isNotNull
    }

    @Test
    fun `Should delete car`() {
        val savedCar = carService.save(createTestCarData()).block()!!
        carService.delete(savedCar.id).block()
        assertThat(carService.get(savedCar.id).block()).isNull()
        assertThat(carService.getMultiple(setOf(savedCar.id)).toList()).isEmpty()
        assertThat(carService.getAll().toList()).isEmpty()
    }

    private fun createTestCarData(
        brand: String = "SuperBrand",
        model: String = "SuperModel",
        producedAt: LocalDate = LocalDate.of(1998, 5, 5)
    ): CarData =
        CarData(brand, model, producedAt)
}