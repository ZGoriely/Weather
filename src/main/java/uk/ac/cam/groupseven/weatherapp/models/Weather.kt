package uk.ac.cam.groupseven.weatherapp.models


import java.time.LocalDateTime

data class Weather(
        val precipitation: Precipitation?,
        val cloudCover: Int?,
        val temperature: Float?,
        val pressure: Float?,
        val humidity: Int?,
        val wind: Wind,
        val fromTime: LocalDateTime,
        val toTime: LocalDateTime) {

    enum class Precipitation {
        NONE, SNOW, RAIN
    }
}
