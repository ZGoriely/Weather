package uk.ac.cam.groupseven.weatherapp.viewmodels

import uk.ac.cam.groupseven.weatherapp.models.Weather
import java.time.LocalDateTime
import java.util.ArrayList

data class DaysViewModel(val times : List<LocalDateTime>, val temperatures : List<Float>, val windSpeeds : List<Float>)
