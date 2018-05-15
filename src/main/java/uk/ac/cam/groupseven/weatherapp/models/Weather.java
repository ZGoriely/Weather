package uk.ac.cam.groupseven.weatherapp.models;


import org.jetbrains.annotations.Nullable;

public class Weather {
    @Nullable
    public final Precipitation precipitation;
    @Nullable
    public final Integer cloudCover;
    @Nullable
    public final Float temperature;
    public final Wind wind;

    public Weather(@Nullable Precipitation precipitation, @Nullable Integer CCover, @Nullable Float temp, Wind w) {
        this.precipitation = precipitation;
        this.cloudCover = CCover;
        this.temperature = temp;
        this.wind = w;
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
                +", temperature: "+this.temperature+", wind: ("+this.wind.toString()+")";
    }
}
