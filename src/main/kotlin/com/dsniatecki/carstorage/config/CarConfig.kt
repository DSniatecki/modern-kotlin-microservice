package com.dsniatecki.carstorage.config

import com.dsniatecki.carstorage.car.CarRepository
import com.dsniatecki.carstorage.car.CarRowRepository
import com.dsniatecki.carstorage.car.CarService
import com.dsniatecki.carstorage.utils.TimeSupplier
import com.dsniatecki.carstorage.utils.createTimeRecorderMetric
import com.dsniatecki.carstorage.utils.withCounter
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.context.annotation.Bean

class CarConfig {

    @Bean
    fun carService(carRepository: CarRepository, timeSupplier: TimeSupplier): CarService =
        CarService(carRepository, timeSupplier)

    @Bean
    fun carRepository(carRowRepository: CarRowRepository, meterRegistry: MeterRegistry): CarRepository =
        CarRepository(
            carRowRepository = carRowRepository,
            findTimeRecorder = meterRegistry.createTimeRecorderMetric(
                "car_query_find_time",
                "Time of query responsible for finding car by id"
            ).withCounter(
                "car_query_find_count",
                "Number of executed queries responsible for finding car by id"
            ),
            findMultipleTimeRecorder = meterRegistry.createTimeRecorderMetric(
                "car_query_find_multiple_time",
                "Time of query responsible for finding multiple cars by ids"
            ).withCounter(
                "car_query_find_multiple_count",
                "Number of executed queries responsible for finding all cars"
            ),
            findAllTimeRecorder = meterRegistry.createTimeRecorderMetric(
                "car_query_find_all_time",
                "Time of query responsible for finding multiple cars by ids"
            ).withCounter(
                "car_query_find_all_count",
                "Number of executed queries responsible for finding all cars"
            ),
            saveTimeRecorder = meterRegistry.createTimeRecorderMetric(
                "car_query_save_time",
                "Time of query responsible for saving car",
            ).withCounter(
                "car_query_save_count",
                "Number of executed queries responsible for saving car"
            ),
            deleteTimeRecorder = meterRegistry.createTimeRecorderMetric(
                "car_query_delete_time",
                "Time of query responsible for deleting car"
            ).withCounter(
                "car_query_delete_count",
                "Number of executed queries responsible for deleting car"
            )
        )
}
