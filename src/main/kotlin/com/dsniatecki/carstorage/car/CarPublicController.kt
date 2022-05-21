package com.dsniatecki.carstorage.car

import com.dsniatecki.carstorage.api.pub.CarsApi
import com.dsniatecki.carstorage.model.pub.CarDTO
import java.util.Optional
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping(path = ["/api/public"])
class CarPublicController(private val carService: CarService) : CarsApi {

    override fun getCar(carId: String, exchange: ServerWebExchange): Mono<ResponseEntity<CarDTO>> =
        carService.get(carId)
            .map { ResponseEntity.ok(it.toPublicDTO()) }
            .switchIfEmpty(Mono.error(NoSuchElementException("Car with id: '$carId' does not exist.")))

    override fun getCars(carIds: Optional<Set<String>>, exchange: ServerWebExchange): Mono<ResponseEntity<Flux<CarDTO>>> =
        Mono.just(carIds.map { carService.getMultiple(it) }.orElseGet { carService.getAll() })
            .map { flux -> flux.map { it.toPublicDTO() } }
            .map { ResponseEntity.ok(it) }
}