package uk.ac.cam.groupseven.weatherapp.models;

public class Weather {
    public final Precipitation precipitation;

    /*TODO: Add
               Wind Object (with speed&dir)
               cloud %
               visibility
               temp
     */

    public Weather(Precipitation precipitation) {

        this.precipitation = precipitation;
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
}
