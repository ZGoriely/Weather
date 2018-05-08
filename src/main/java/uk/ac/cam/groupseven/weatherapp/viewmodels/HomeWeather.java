package uk.ac.cam.groupseven.weatherapp.viewmodels;

public class HomeWeather {
    public final String flagText;
    public final String weatherText;
    public final Boolean loading;
    public final Throwable error;

    public HomeWeather(String flagText, String weatherText) {
        this.flagText = flagText;
        this.weatherText = weatherText;
        loading = false;
        error = null;
    }

    private HomeWeather() {
        this.flagText = null;
        this.weatherText = null;
        loading = true;
        error = null;
    }

    public HomeWeather(Throwable throwable) {
        this.flagText = null;
        this.weatherText = null;
        loading = false;
        error = throwable;
    }

    public static HomeWeather Loading() {
        return new HomeWeather();
    }


}
