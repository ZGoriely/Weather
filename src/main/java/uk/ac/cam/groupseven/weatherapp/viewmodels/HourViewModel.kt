package uk.ac.cam.groupseven.weatherapp.viewmodels

import java.time.LocalDateTime

data class HourViewModel(
        val currentTime: LocalDateTime,
        val hourlyWeather: List<HourlyWeather>
)

data class HourlyWeather(
        val time: LocalDateTime,
        val temperature: Float,
        val windSpeed: Float
)