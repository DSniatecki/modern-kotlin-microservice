package com.dsniatecki.carstorage.car

import com.dsniatecki.carstorage.model.internal.CarDTO as CarInternalDTO
import com.dsniatecki.carstorage.model.pub.CarDTO as CarPublicDTO
import com.dsniatecki.carstorage.model.internal.CarDataDTO

fun Car.toInternalDTO(): CarInternalDTO =
    CarInternalDTO()
        .id(this.id)
        .brand(this.brand)
        .model(this.model)
        .producedAt(this.producedAt)
        .createdAt(this.createdAt)
        .updatedAt(this.updatedAt)

fun Car.toPublicDTO(): CarPublicDTO =
    CarPublicDTO()
        .id(this.id)
        .brand(this.brand)
        .model(this.model)
        .producedAt(this.producedAt)
        .createdAt(this.createdAt)
        .updatedAt(this.updatedAt);

fun CarDataDTO.toCarData(): CarData =
    CarData(
        brand = this.brand,
        model = this.model,
        producedAt = this.producedAt
    )
