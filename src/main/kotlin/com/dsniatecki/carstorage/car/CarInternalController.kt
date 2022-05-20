package com.dsniatecki.carstorage.car

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON
import org.reactivestreams.Publisher
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/api/internal"])
class CarInternalController(
    private val carService: CarService
) {

    @GetMapping(value = ["/cars/{carId}"], produces = [JSON])
    fun getCar(@PathVariable(name = "carId") carId: String): Publisher<Car> =
        carService.get(carId)
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))
            .onErrorMap({ it is NoSuchElementException }) { ResponseStatusException(HttpStatus.NOT_FOUND, it.message) }

    @GetMapping(value = ["/cars"], produces = [JSON])
    fun getCars(
        @RequestParam(name = "carIds", required = false) carIds: Set<String>?
    ): Publisher<Car> =
        if (carIds.isNullOrEmpty()) carService.getAll() else carService.getMultiple(carIds)

    @PostMapping(value = ["/cars"], consumes = [JSON], produces = [JSON])
    @ResponseStatus(HttpStatus.CREATED)
    fun createCar(@RequestBody newCarData: CarData): Publisher<Car> =
        Mono.just(newCarData)
            .map { it.validate() }
            .flatMap { carService.save(newCarData) }
            .handleErrors()

    @PutMapping(value = ["/cars/{carId}"], consumes = [JSON], produces = [JSON])
    fun updateCar(
        @PathVariable(name = "carId") carId: String,
        @RequestBody carData: CarData
    ): Publisher<Car> =
        Mono.just(carData)
            .map { it.validate() }
            .flatMap { carService.update(carId, carData) }
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))
            .handleErrors()

    @DeleteMapping(value = ["/cars/{carId}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCar(@PathVariable(name = "carId") carId: String): Publisher<Unit> =
        carService.delete(carId)
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))
            .handleErrors()

    private fun <T> Mono<T>.handleErrors(): Mono<T> =
        this.onErrorMap({ it is NoSuchElementException }) { ResponseStatusException(HttpStatus.NOT_FOUND, it.message) }
            .onErrorMap({ it is IllegalStateException }) { ResponseStatusException(HttpStatus.BAD_REQUEST, it.message) }
            .onErrorMap({ it is IllegalArgumentException }) { ResponseStatusException(HttpStatus.BAD_REQUEST, it.message) }
            .onErrorMap({ it is DataIntegrityViolationException }) { ResponseStatusException(HttpStatus.CONFLICT, it.message) }
}