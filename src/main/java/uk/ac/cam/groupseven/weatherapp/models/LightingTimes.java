package uk.ac.cam.groupseven.weatherapp.models;

public class LightingTimes {
    public final String todayUpTime;
    public final String todayDownTime;
    public final String tomorrowUpTime;
    public final String tomorrowDownTime;

    public LightingTimes(String todayUp, String todayDown, String tomorrowUp, String tomorrowDown) {
        todayUpTime = todayUp;
        todayDownTime = todayDown;
        tomorrowUpTime = tomorrowUp;
        tomorrowDownTime = tomorrowDown;
    }
}
