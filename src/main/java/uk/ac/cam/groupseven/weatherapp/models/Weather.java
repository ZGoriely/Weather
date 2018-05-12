package uk.ac.cam.groupseven.weatherapp.models;

public class Weather {
    public final Precipitation precipitation;
    public final int cloudCover;
    public final float temperature;
    public final Wind wind;

    /*TODO: Add
               Wind Object (with speed&dir)
               cloud %
               temp
     */

    public Weather(Precipitation precipitation, int CCover, float temp, Wind w) {
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
        return "precipitation: "+this.precipitation.toString()+", cloudCover: "+this.cloudCover
                +", temperature: "+this.temperature+", wind: ("+this.wind.toString()+")";
    }
}
