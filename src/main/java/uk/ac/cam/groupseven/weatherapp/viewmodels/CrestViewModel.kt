package uk.ac.cam.groupseven.weatherapp.viewmodels

import javax.swing.*
import java.util.TreeMap

data class CrestViewModel(
    val images: TreeMap<String, ImageIcon>)