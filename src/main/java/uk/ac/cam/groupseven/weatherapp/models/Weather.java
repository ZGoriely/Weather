package uk.ac.cam.groupseven.weatherapp.models;


import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

public class Weather {
    @Nullable
    public final Precipitation precipitation;
    @Nullable
    public final Integer cloudCover;
    @Nullable
    public final Float temperature;
    @Nullable
    public final Float pressure;
    @Nullable
    public final Integer humidity;
    public final Wind wind;
    public final LocalDateTime fromTime;
    public final LocalDateTime toTime;

    public Weather(@Nullable Precipitation precipitation, @Nullable Integer CCover, @Nullable Float temp, @Nullable Float pressure, @Nullable Integer humidity, Wind w, LocalDateTime fromTime, LocalDateTime toTime) {
        this.precipitation = precipitation;
        this.cloudCover = CCover;
        this.temperature = temp;
        this.wind = w;
        this.pressure = pressure;
        this.humidity = humidity;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public enum Precipitation {NONE, SNOW, RAIN}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;

        return precipitation == weather.precipitation;
    }

    @Override
    public int hashCode() {
        return precipitation != null ? precipitation.hashCode() : 0;
    }

    @Override
    public String toString(){
        return "precipitation: " + (this.precipitation != null ? this.precipitation.toString() : "null") + ", cloudCover: " + this.cloudCover
                +", temperature: "+this.temperature+", pressure: "+this.pressure+", humidity: "+this.humidity+", wind: ("+this.wind.toString()
                +"), from: "+this.fromTime+", to: "+this.toTime;
    }
}
