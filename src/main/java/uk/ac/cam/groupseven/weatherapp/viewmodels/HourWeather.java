package uk.ac.cam.groupseven.weatherapp.viewmodels;

import java.util.ArrayList;
import java.util.List;

public class HourWeather {

    public final ArrayList<String> precipitationTexts;
    public final boolean loading;
    public final Throwable error;

    public HourWeather(List<String> precipitationTexts) {
        this.precipitationTexts = new ArrayList<>(precipitationTexts);
        loading = false;
        error = null;
    }

    private HourWeather() {
        this.precipitationTexts = null;
        loading = true;
        error = null;
    }

    public HourWeather(Throwable throwable) {
        this.precipitationTexts = null;
        loading = false;
        error = throwable;
    }

    public static HourWeather Loading() {
        return new HourWeather();
    }
}
