package uk.ac.cam.groupseven.weatherapp.viewmodels

import uk.ac.cam.groupseven.weatherapp.models.FlagStatus
import javax.swing.ImageIcon


data class HomeViewModel(
        val flag: FlagStatus,
        val flagImage: ImageIcon,
        val temperature: Float,
        val windSpeed: Float,
        val windDir: String)
