package com.dsniatecki.carstorage.car

import com.dsniatecki.carstorage.api.internal.CarsApi
import com.dsniatecki.carstorage.model.internal.CarDto
import com.dsniatecki.carstorage.model.internal.CarDataDto
import java.util.Optional
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/api/internal"])
class CarInternalController(private val carService: CarService) : CarsApi {

    override fun getCar(carId: String, exchange: ServerWebExchange): Mono<ResponseEntity<CarDto>> =
        carService.get(carId)
            .map { ResponseEntity.ok(it.toInternalDto()) }
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))

    override fun getCars(carIds: Optional<Set<String>>, exchange: ServerWebExchange): Mono<ResponseEntity<Flux<CarDto>>> =
        Mono.just(carIds.map { carService.getMultiple(it) }.orElseGet { carService.getAll() })
            .map { flux -> flux.map { it.toInternalDto() } }
            .map { ResponseEntity.ok(it) }

    override fun createCar(carDataDto: Mono<CarDataDto>, exchange: ServerWebExchange): Mono<ResponseEntity<CarDto>> =
        carDataDto.flatMap { carService.save(it.toCarData()) }
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it.toInternalDto()) }

    override fun updateCar(carId: String, carDataDto: Mono<CarDataDto>, exchange: ServerWebExchange): Mono<ResponseEntity<CarDto>> =
        carDataDto.flatMap { carService.update(carId, it.toCarData()) }
            .map { ResponseEntity.ok(it.toInternalDto()) }
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))

    override fun deleteCar(carId: String, exchange: ServerWebExchange): Mono<ResponseEntity<Void>> =
        carService.delete(carId)
            .map { ResponseEntity.noContent().build<Void>() }
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))
}