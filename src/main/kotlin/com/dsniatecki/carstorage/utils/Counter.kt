package com.dsniatecki.carstorage.utils

@FunctionalInterface
interface Counter {
    fun add(number: Int)
    fun increment() {
        add(1)
    }
}

