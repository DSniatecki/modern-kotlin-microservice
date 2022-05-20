package com.dsniatecki.carstorage.car

import com.dsniatecki.carstorage.utils.TimeRecorder
import com.dsniatecki.carstorage.utils.recorded
import java.time.LocalDateTime
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class CarRepository(
    private val carRowRepository: CarRowRepository,
    private val findTimeRecorder: TimeRecorder,
    private val findMultipleTimeRecorder: TimeRecorder,
    private val findAllTimeRecorder: TimeRecorder,
    private val saveTimeRecorder: TimeRecorder,
    private val deleteTimeRecorder: TimeRecorder,
) {
    fun findById(objectId: String): Mono<Car> =
        carRowRepository.findById(objectId)
            .recorded(findTimeRecorder)
            .map { mapFromRow(it) }

    fun findByIds(objectIds: Set<String>): Flux<Car> =
        carRowRepository
            .findByIds(objectIds)
            .recorded(findMultipleTimeRecorder)
            .map { mapFromRow(it) }

    fun findAll(): Flux<Car> =
        carRowRepository
            .findAll()
            .recorded(findAllTimeRecorder)
            .map { mapFromRow(it) }

    fun save(car: Car): Mono<Car> =
        carRowRepository.save(mapToRow(car))
            .recorded(saveTimeRecorder)
            .map { mapFromRow(it) }

    fun delete(objectId: String, deleted_at: LocalDateTime): Mono<Unit> =
        carRowRepository.delete(objectId, deleted_at)
            .recorded(deleteTimeRecorder)

    private fun mapFromRow(row: CarRow): Car =
        Car(
            id = row.id,
            brand = row.brand,
            model = row.model,
            producedAt = row.producedAt,
            createdAt = row.createdAt,
            updatedAt = row.updatedAt
        )

    private fun mapToRow(car: Car): CarRow =
        CarRow(
            id = car.id,
            brand = car.brand,
            model = car.model,
            producedAt = car.producedAt,
            createdAt = car.createdAt,
            updatedAt = car.updatedAt,
            isDeleted = false
        )
}