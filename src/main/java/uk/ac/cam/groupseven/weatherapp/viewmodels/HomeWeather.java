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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HomeWeather that = (HomeWeather) o;

        if (flagText != null ? !flagText.equals(that.flagText) : that.flagText != null) return false;
        if (weatherText != null ? !weatherText.equals(that.weatherText) : that.weatherText != null) return false;
        if (loading != null ? !loading.equals(that.loading) : that.loading != null) return false;
        return error != null ? error.equals(that.error) : that.error == null;
    }

    @Override
    public int hashCode() {
        int result = flagText != null ? flagText.hashCode() : 0;
        result = 31 * result + (weatherText != null ? weatherText.hashCode() : 0);
        result = 31 * result + (loading != null ? loading.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

}
