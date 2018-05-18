package uk.ac.cam.groupseven.weatherapp.viewmodels

data class HourViewModel(
        val currentTime: String,
        val hourlyWeather: List<HourlyWeather>
)

data class HourlyWeather(
        val timeText: String,
        val temperatureText: String,
        val windText: String
)