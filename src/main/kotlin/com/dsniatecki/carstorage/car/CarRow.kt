package com.dsniatecki.carstorage.car

import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("car")
data class CarRow(
    @Id @Column("id") private val id: String,
    @Column("brand") val brand: String,
    @Column("model") val model: String,
    @Column("produced_at") val producedAt: LocalDate,
    @Column("created_at") val createdAt: LocalDateTime,
    @Column("updated_at") val updatedAt: LocalDateTime?,
    @Column("is_deleted") val isDeleted: Boolean,
) : Persistable<String> {
    override fun isNew() = updatedAt == null
    override fun getId(): String = id
}