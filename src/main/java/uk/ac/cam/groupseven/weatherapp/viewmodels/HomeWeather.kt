package uk.ac.cam.groupseven.weatherapp.viewmodels

data class HomeWeather(
    val flagText: String?,
    val weatherText: String?,
    val loading: Boolean,
    val error: Throwable?)
{

    constructor(flagText: String, weatherText: String) : this(flagText, weatherText, false, null)
    private constructor() : this(null, null, true, null)
    constructor(throwable: Throwable) : this(null, null, true, throwable)

    companion object {

        fun loading(): HomeWeather {
            return HomeWeather()
        }
    }

}
