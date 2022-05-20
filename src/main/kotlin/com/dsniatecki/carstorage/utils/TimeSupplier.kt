package com.dsniatecki.carstorage.utils

import java.time.LocalDateTime

@FunctionalInterface
interface TimeSupplier {
    fun now(): LocalDateTime
}