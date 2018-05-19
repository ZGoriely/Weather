package uk.ac.cam.groupseven.weatherapp.viewmodels

import uk.ac.cam.groupseven.weatherapp.models.FlagStatus
import java.awt.Image
import javax.swing.ImageIcon


data class HomeViewModel(
        val flag: FlagStatus,
        val flagImage: HomeViewModelImageIcon,
        val temperature: Float,
        val windSpeed: Float,
        val windDir: String)

class HomeViewModelImageIcon(image: Image) : ImageIcon(image) {
    override fun equals(other: Any?): Boolean {
        return other is HomeViewModelImageIcon
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}