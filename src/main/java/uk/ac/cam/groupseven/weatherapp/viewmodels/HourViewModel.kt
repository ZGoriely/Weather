package uk.ac.cam.groupseven.weatherapp.viewmodels

data class HourViewModel(
        val currentTime: String,
        val hourlyWeather: MutableList<HourlyWeather>
)

data class HourlyWeather(
        val time: String,
        val temperature: String,
        val windSpeed: String
)