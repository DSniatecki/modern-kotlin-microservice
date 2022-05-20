package com.dsniatecki.carstorage.car

import java.time.LocalDate
import java.time.LocalDateTime

data class Car(
    val id: String,
    val brand: String,
    val model: String,
    val producedAt: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)