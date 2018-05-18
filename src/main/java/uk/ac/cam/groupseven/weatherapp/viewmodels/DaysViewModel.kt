package uk.ac.cam.groupseven.weatherapp.viewmodels

data class DaysViewModel(val dayWeathers: List<DayWeather>)

data class DayWeather(val date: String,
                      val morningTemperature: String,
                      val morningWind: String,
                      val afternoonTemperature: String,
                      val afternoonWind: String)
