package com.dsniatecki.carstorage

import com.dsniatecki.carstorage.config.CarConfig
import com.dsniatecki.carstorage.config.FlywayConfig
import com.dsniatecki.carstorage.config.SecurityConfig
import com.dsniatecki.carstorage.config.UtilsConfig
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@SpringBootApplication
@Import(UtilsConfig::class, SecurityConfig::class, FlywayConfig::class, CarConfig::class)
class CarStorageApplication

fun main(args: Array<String>) {
    runApplication<CarStorageApplication>(*args)
}
