package uk.ac.cam.groupseven.weatherapp.models;

public class Weather {
    public final Precipitation precipitation;

    public Weather(Precipitation precipitation) {

        this.precipitation = precipitation;
    }

    public enum Precipitation {NONE, SNOW, RAIN}
}
