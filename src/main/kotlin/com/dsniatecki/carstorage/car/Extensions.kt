package com.dsniatecki.carstorage.car

import java.util.UUID

const val minBrandLength = 1
const val maxBrandLength = 64

const val minModelLength = 1
const val maxModelLength = 256

fun generateId(): String = UUID.randomUUID().toString()

fun CarData.validate(): CarData {
    validateBrand(this.brand)
    validateModel(this.model)
    return this
}

private fun validateBrand(name: String) {
    if (name.length < minBrandLength || name.length > maxBrandLength) {
        throw IllegalStateException("Car brand is not valid.")
    }
}

private fun validateModel(name: String) {
    if (name.length < minModelLength || name.length > maxModelLength) {
        throw IllegalStateException("Car model is not valid.")
    }
}