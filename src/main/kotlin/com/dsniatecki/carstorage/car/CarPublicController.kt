package com.dsniatecki.carstorage.car

import org.springframework.http.MediaType.APPLICATION_JSON_VALUE as JSON
import org.reactivestreams.Publisher
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/api/public"])
class CarPublicController(
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
}