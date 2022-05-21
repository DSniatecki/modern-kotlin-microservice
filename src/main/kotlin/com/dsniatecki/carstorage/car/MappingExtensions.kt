package com.dsniatecki.carstorage.car

import com.dsniatecki.carstorage.model.internal.CarDto as CarInternalDto
import com.dsniatecki.carstorage.model.pub.CarDto as CarPublicDto
import com.dsniatecki.carstorage.model.internal.CarDataDto

fun Car.toInternalDto(): CarInternalDto =
    CarInternalDto()
        .id(this.id)
        .brand(this.brand)
        .model(this.model)
        .producedAt(this.producedAt)
        .createdAt(this.createdAt)
        .updatedAt(this.updatedAt)

fun Car.toPublicDto(): CarPublicDto =
    CarPublicDto()
        .id(this.id)
        .brand(this.brand)
        .model(this.model)
        .producedAt(this.producedAt)
        .createdAt(this.createdAt)
        .updatedAt(this.updatedAt);

fun CarDataDto.toCarData(): CarData =
    CarData(
        brand = this.brand,
        model = this.model,
        producedAt = this.producedAt
    )
