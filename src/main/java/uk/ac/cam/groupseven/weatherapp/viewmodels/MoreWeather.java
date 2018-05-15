package uk.ac.cam.groupseven.weatherapp.viewmodels;

import uk.ac.cam.groupseven.weatherapp.models.LightingTimes;
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel;
import uk.ac.cam.groupseven.weatherapp.models.Weather;

public class MoreWeather {

    public final boolean loading;
    public final Throwable error;
    public final Weather weather;
    public final WaterLevel waterLevel;
    public final LightingTimes lightingTimes;

    public MoreWeather(Weather weather, WaterLevel waterLevel, LightingTimes lightingTimes) {
        loading = false;
        error = null;
        this.weather = weather;
        this.waterLevel = waterLevel;
        this.lightingTimes = lightingTimes;
    }

    public MoreWeather() {
        loading = true;
        error = null;
        weather = null;
        waterLevel = null;
        lightingTimes = null;
    }

    public MoreWeather(Throwable error) {
        loading = false;
        this.error = error;
        weather = null;
        waterLevel = null;
        lightingTimes = null;
    }

    public static MoreWeather Loading() {
        return new MoreWeather();
    }
}
