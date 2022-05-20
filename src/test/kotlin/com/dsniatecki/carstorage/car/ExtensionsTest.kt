package com.dsniatecki.carstorage.car

import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class ExtensionsTest {

    private val testDate = LocalDate.of(1998, 5, 5)

    @Test
    fun `Should pass CarData validation`() {
        val carData = CarData("SuperBrand", "SuperModel", testDate)
        assertThat(carData.validate()).isEqualTo(carData)
    }

    @Test
    fun `Should not pass CarData validation due to invalid car brand`() {
        val carData1 = CarData("", "SuperModel", testDate)
        val carData2 = CarData("A".repeat(maxBrandLength + 1), "SuperModel", testDate)
        assertAll(
            { assertThrows(IllegalStateException::class.java) { carData1.validate() } },
            { assertThrows(IllegalStateException::class.java) { carData2.validate() } }
        )
    }

    @Test
    fun `Should not pass CarData validation due to invalid car model`() {
        val carData1 = CarData("SuperBrand", "", testDate)
        val carData2 = CarData("SuperBrand", "A".repeat(maxModelLength + 1), testDate)
        assertAll(
            { assertThrows(IllegalStateException::class.java) { carData1.validate() } },
            { assertThrows(IllegalStateException::class.java) { carData2.validate() } }
        )
    }
}