package com.dsniatecki.carstorage.car

import com.dsniatecki.carstorage.api.internal.CarsApi
import com.dsniatecki.carstorage.model.internal.CarDTO
import com.dsniatecki.carstorage.model.internal.CarDataDTO
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

    override fun getCar(carId: String, exchange: ServerWebExchange): Mono<ResponseEntity<CarDTO>> =
        carService.get(carId)
            .map { ResponseEntity.ok(it.toInternalDTO()) }
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))

    override fun getCars(carIds: Optional<Set<String>>, exchange: ServerWebExchange): Mono<ResponseEntity<Flux<CarDTO>>> =
        Mono.just(carIds.map { carService.getMultiple(it) }.orElseGet { carService.getAll() })
            .map { flux -> flux.map { it.toInternalDTO() } }
            .map { ResponseEntity.ok(it) }

    override fun createCar(carDataDTO: Mono<CarDataDTO>, exchange: ServerWebExchange): Mono<ResponseEntity<CarDTO>> =
        carDataDTO.flatMap { carService.save(it.toCarData()) }
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it.toInternalDTO()) }

    override fun updateCar(carId: String, carDataDTO: Mono<CarDataDTO>, exchange: ServerWebExchange): Mono<ResponseEntity<CarDTO>> =
        carDataDTO.flatMap { carService.update(carId, it.toCarData()) }
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it.toInternalDTO()) }

    override fun deleteCar(carId: String, exchange: ServerWebExchange): Mono<ResponseEntity<Void>> =
        carService.delete(carId)
            .map { ResponseEntity.noContent().build<Void>() }
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))
}