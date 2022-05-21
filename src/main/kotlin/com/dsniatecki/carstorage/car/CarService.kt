package com.dsniatecki.carstorage.car

import com.dsniatecki.carstorage.utils.TimeSupplier
import com.dsniatecki.carstorage.utils.generateId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CarService(
    private val carRepository: CarRepository,
    private val timeSupplier: TimeSupplier,
) {

    fun get(carId: String): Mono<Car> = carRepository.findById(carId)

    fun getMultiple(carIds: Set<String>): Flux<Car> =
        carRepository.findByIds(carIds).sort(Comparator.comparing { it.createdAt })

    fun getAll(): Flux<Car> =
        carRepository.findAll().sort(Comparator.comparing { it.createdAt })

    fun save(carData: CarData): Mono<Car> = carRepository.save(createNewCar(carData))

    fun update(carId: String, carData: CarData): Mono<Car> =
        carRepository.findById(carId)
            .flatMap { carRepository.save(updateCar(it, carData)) }

    fun delete(objectId: String): Mono<Unit> =
        carRepository.findById(objectId)
            .flatMap { carRepository.delete(objectId, timeSupplier.now()).switchIfEmpty(Mono.just(Unit)) }

    private fun createNewCar(carData: CarData): Car =
        Car(
            id = generateId(),
            brand = carData.brand,
            model = carData.model,
            producedAt = carData.producedAt,
            createdAt = timeSupplier.now(),
            updatedAt = null
        )

    private fun updateCar(car: Car, carData: CarData): Car =
        car.copy(
            brand = carData.brand,
            model = carData.model,
            producedAt = carData.producedAt,
            updatedAt = timeSupplier.now()
        )
}