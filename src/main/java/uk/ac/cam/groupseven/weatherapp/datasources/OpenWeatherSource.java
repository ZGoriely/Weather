package uk.ac.cam.groupseven.weatherapp.datasources;


import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.models.Wind;

import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class OpenWeatherSource implements WeatherSource {

    @Inject
    @Named("openWeatherApiUrl")
    private URL apiUrl;
    private InputStream apiResponse;

    @Override
    public Observable<Weather> getWeatherNow() {

        /* There are a lot of for loops as the XML parsing system seems to create a lot of NodeLists, which you can only iterate through */
        return Observable.fromCallable(() -> {

            Weather currentWeather = null;
            apiResponse = apiUrl.openStream();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document weatherDataDoc = builder.parse(apiResponse);
            NodeList readings = weatherDataDoc.getElementsByTagName("time");

            LocalDateTime currentTime = LocalDateTime.now();

            for(int i = 0; i < readings.getLength(); i++){
                if(isCorrectReading(currentTime, parseTimeNode(readings.item(i)))) {
                    currentWeather = parseTimeNode(readings.item(i));
                }
            }

            return currentWeather;
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }


    //TODO: Implement methods for getting future weather
    @Override
    public Observable<Weather> getWeatherInHours(int hours) {
        return Observable.fromCallable(() -> {
            apiResponse = apiUrl.openStream();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document weatherDataDoc = builder.parse(apiResponse);
            NodeList readings = weatherDataDoc.getElementsByTagName("time");

            Weather forecast = null;

            LocalDateTime forecastTime = LocalDateTime.now().plusHours((long)hours);

            for(int i = 0; i < readings.getLength(); i++){
                if(isCorrectReading(forecastTime, parseTimeNode(readings.item(i)))) {
                    forecast = parseTimeNode(readings.item(i));
                }
            }

            return forecast;

        })
                .observeOn(Schedulers.io());
    }

    @Override
    public Observable<Weather> getWeatherInDays(int days, int timeInHours) { //TODO deal with 5 days in future but time later than now in day so no forecast.
        return Observable.fromCallable(() -> {
            apiResponse = apiUrl.openStream();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document weatherDataDoc = builder.parse(apiResponse);
            NodeList readings = weatherDataDoc.getElementsByTagName("time");

            Weather forecast = null;

            if(timeInHours >= 24 || timeInHours < 0) throw new TimeOutOfRangeException(timeInHours);

            if(days > 5) throw new CrystalBallDepthExceededException(days, timeInHours);
            if(days == 5
                    && ((LocalDateTime.now().getHour() / 3) * 3 != timeInHours) /* Check that requested time isn't on the boundary of latest available forecast */
                    && (LocalDateTime.now().getHour() / 3 <= timeInHours / 3)) /* Check if requested time is outside the boundaries of the currently available readings (5 days in 3h steps) */
                throw new CrystalBallDepthExceededException(days, timeInHours);

            LocalDateTime forecastTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays((long)days).plusHours((long)timeInHours);

            for(int i = 0; i < readings.getLength(); i++){
                if(isCorrectReading(forecastTime, parseTimeNode(readings.item(i)))) {
                    forecast = parseTimeNode(readings.item(i));
                }
            }

            if(forecast == null) {
                throw new ForecastNotAvailableException();
            }

            return forecast;

        })
                .observeOn(Schedulers.io());
    }

    private class ForecastNotAvailableException extends RuntimeException{
        public ForecastNotAvailableException() {
            super("This forecast isn't available, not really sure why. You could be asking for a weird time, like in the past or something, or it could be a bug in this code.");
        }
    }

    private class CrystalBallDepthExceededException extends RuntimeException {
        private final int dayValue;
        private final int timeValue;

        public CrystalBallDepthExceededException(int dayValue, int timeValue){
            super("Requested weather "+dayValue+" days and "+(timeValue - LocalDateTime.now().getHour())+" hours in the future ("+ timeValue+":00).\r\n Sadly, we're not *that* good - we're using the free API and can only forecast until (floor(current time / 3) * 3) o'clock on the 5th day ahead.");
            this.dayValue = dayValue;
            this.timeValue = timeValue;
        }

        public int getDayValue() {
            return dayValue;
        }

        public int getTImeValue() {
            return timeValue;
        }
    }

    private class TimeOutOfRangeException extends RuntimeException {
        private final int badTimeValue;

        public TimeOutOfRangeException(int badTimeValue){
            super("Requested weather at "+badTimeValue+" o'clock. Pretty sure that's not a time of day.");
            this.badTimeValue = badTimeValue;
        }

        public int getBadTimeValue() {
            return badTimeValue;
        }
    }

    private class UnsupportedPrecipitationException extends RuntimeException {
        private final String value;

        UnsupportedPrecipitationException(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private boolean isCorrectReading(LocalDateTime currentTime, Weather currentWeather){
        return (currentTime.compareTo(currentWeather.fromTime) >= 0 && currentTime.compareTo(currentWeather.toTime) <= 0); /* 'from' of next reading is same as 'to' of this reading (i.e. the boundary times overlap) but the iterating-through-readings code will just use the more 'in the future' reading */
    }

    private Weather parseTimeNode(Node timeNode) {

        Weather.Precipitation currentPrecipitation = Weather.Precipitation.NONE;
        Integer currentCloudCover = null;
        Integer currentHumidity = null;
        Float currentTemperature = null;
        Float currentPressure = null;
        Wind currentWind;
        Float windSpeed = null;
        String windDir = null;
        LocalDateTime from = null;
        LocalDateTime to = null;

        NamedNodeMap timeAttrs = timeNode.getAttributes();
        for (int i = 0; i < timeAttrs.getLength(); i++) {
            Node attr = timeAttrs.item(i);
            if (attr.getNodeName().equals("from")) {
                from = LocalDateTime.parse(attr.getNodeValue());
            }
            if (attr.getNodeName().equals("to")) {
                to = LocalDateTime.parse(attr.getNodeValue());
            }
        }

        for (int i = 0; i < timeNode.getChildNodes().getLength(); i++) { /* Get first forecast in response (current-ish) */
            Node dataNode = timeNode.getChildNodes().item(i);
            if (dataNode.getNodeName().equals("temperature")) {
                currentTemperature = Float.parseFloat(dataNode.getAttributes().getNamedItem("value").getNodeValue()); /* TODO Careful of parsing floats etc */
            }
            if (dataNode.getNodeName().equals("pressure")) {
                currentPressure = Float.parseFloat(dataNode.getAttributes().getNamedItem("value").getNodeValue()); /* TODO Careful of parsing floats etc */
            }
            if (dataNode.getNodeName().equals("windDirection")) {
                windDir = dataNode.getAttributes().getNamedItem("code").getNodeValue();
            }
            if (dataNode.getNodeName().equals("windSpeed")) {
                windSpeed = Float.parseFloat(dataNode.getAttributes().getNamedItem("mps").getNodeValue());
            }
            if (dataNode.getNodeName().equals("clouds")) {
                currentCloudCover = Integer.parseInt(dataNode.getAttributes().getNamedItem("all").getNodeValue());
            }
            if (dataNode.getNodeName().equals("humidity")) {
                currentHumidity = Integer.parseInt(dataNode.getAttributes().getNamedItem("value").getNodeValue());
            }
            if (dataNode.getNodeName().equals("precipitation")) { /* DISCLAIMER: THERE MAY BE MORE TYPES OF PRECIPITATION. TODO investigate this. */
                NamedNodeMap attrs = dataNode.getAttributes();

                for (int j = 0; j < attrs.getLength(); j++) {
                    Node attr = attrs.item(j);
                    if (attr.getNodeName().equals("type")) {
                        String value = attr.getNodeValue();
                        switch (value) {
                            case "rain":
                                currentPrecipitation = Weather.Precipitation.RAIN;
                                break;
                            case "snow":
                                currentPrecipitation = Weather.Precipitation.SNOW;
                                break;
                            default:
                                throw new UnsupportedPrecipitationException(value);
                        }
                    }
                }
            }
        }

        currentWind = new Wind(windSpeed, windDir);
        return new Weather(currentPrecipitation, currentCloudCover, currentTemperature, currentPressure, currentHumidity, currentWind, from, to);
    }

    public static boolean forecastAvailable(int days, int timeInHours){
        boolean inRange = true;

        if(timeInHours >= 24 || timeInHours < 0) inRange = false;

        if(days > 5) inRange = false;
        if(days == 5
                && ((LocalDateTime.now().getHour() / 3) * 3 != timeInHours) /* Check that requested time isn't on the boundary of latest available forecast */
                && (LocalDateTime.now().getHour() / 3 <= timeInHours / 3)) /* Check that requested time is outside the boundaries of the currently available readings (5 days in 3h steps) */
            inRange = false;

        if(days == 0
                && ((LocalDateTime.now().getHour() / 3) * 3 != timeInHours) /* Check that requested time isn't on the boundary of earliest available forecast */
                && (LocalDateTime.now().getHour() / 3 > timeInHours / 3)) /* Check that requested time is located in the time window before the current time */

        return inRange;
    }
}
