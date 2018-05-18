package uk.ac.cam.groupseven.weatherapp.viewmodels

import uk.ac.cam.groupseven.weatherapp.models.FlagStatus


data class HomeViewModel(
    val flag: FlagStatus,
    val temperature: Float,
    val windSpeed: Float,
    val windDir: String)
