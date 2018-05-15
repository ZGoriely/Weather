package uk.ac.cam.groupseven.weatherapp.viewmodels;

import java.util.ArrayList;
import java.util.List;

public class DaysWeather {

    // TODO: Create DaysWeather (Copied HourWeather for use in DaysScreen)

    public final ArrayList<String> precipitationTexts;
    public final boolean loading;
    public final Throwable error;

    public DaysWeather(List<String> precipitationTexts) {
        this.precipitationTexts = new ArrayList<>(precipitationTexts);
        loading = false;
        error = null;
    }

    private DaysWeather() {
        this.precipitationTexts = null;
        loading = true;
        error = null;
    }

    public DaysWeather(Throwable throwable) {
        this.precipitationTexts = null;
        loading = false;
        error = throwable;
    }

    public static DaysWeather Loading() {
        return new DaysWeather();
    }
}

