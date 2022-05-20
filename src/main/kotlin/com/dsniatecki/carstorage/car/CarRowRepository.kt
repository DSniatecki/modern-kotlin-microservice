package com.dsniatecki.carstorage.car

import java.time.LocalDateTime
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CarRowRepository : ReactiveCrudRepository<CarRow, String> {

    @Query("SELECT * FROM car WHERE id = :id AND is_deleted = FALSE")
    override fun findById(id: String): Mono<CarRow>

    @Query("SELECT * FROM car WHERE id IN (:ids) AND is_deleted = FALSE")
    fun findByIds(ids: Set<String>): Flux<CarRow>

    @Query("SELECT * FROM car WHERE is_deleted = FALSE")
    override fun findAll(): Flux<CarRow>

    @Query("UPDATE car SET updated_at = :deleted_at, is_deleted = TRUE WHERE id = :id AND is_deleted = FALSE")
    fun delete(id: String, deleted_at: LocalDateTime): Mono<Unit>
}