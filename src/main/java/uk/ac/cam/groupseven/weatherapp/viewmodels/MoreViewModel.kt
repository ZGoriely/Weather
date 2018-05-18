package uk.ac.cam.groupseven.weatherapp.viewmodels

import uk.ac.cam.groupseven.weatherapp.models.LightingTimes
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel
import uk.ac.cam.groupseven.weatherapp.models.Weather

data class MoreViewModel(
        val weather: Weather,
        val waterLevel: WaterLevel,
        val lightingLimes: LightingTimes)
