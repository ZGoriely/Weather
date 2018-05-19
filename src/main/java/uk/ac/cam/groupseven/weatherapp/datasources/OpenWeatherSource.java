package uk.ac.cam.groupseven.weatherapp.datasources;


import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.models.Wind;

import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

/* Class providing methods for getting weather from the OpenWeatherMap API (via the project's API key) */

public class OpenWeatherSource implements WeatherSource {

    @Inject
    @Named("openWeatherApiUrl")
    private URL apiUrl; // URL of OpenWeatherMap's API (containing parameters and API key etc)
    private String weatherXml; // A local cache of the XML content of the last API call - the free API only allows us to make one query every 6 seconds so caching is necessary

    @NonNull
    private LocalDateTime lastUpdated = LocalDateTime.MIN; // Stores the time when the cached XML was last updated

    private String getWeatherXml() throws IOException { /* Gets a string containing the XML of the API request. */
        if (weatherXml == null || lastUpdated.until(LocalDateTime.now(), ChronoUnit.SECONDS) >= 6) { // If it has been at least 6 seconds since the cache was last updated, reload the data from the website.
            lastUpdated = LocalDateTime.now();
            weatherXml = new BufferedReader(new InputStreamReader(apiUrl.openStream())).lines().collect(Collectors.joining());
        }
        return weatherXml;
    }

    @Override
    public Observable<Weather> getWeatherNow() { /* Returns an Observable that provides the current weather data as a stream of Weather objects */

        // There are a lot of for loops as the XML parsing system seems to create a lot of NodeLists, which you can only iterate through
        return Observable.fromCallable(() -> {

            Weather currentWeather = null;
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document weatherDataDoc = builder.parse(new InputSource(new StringReader(getWeatherXml()))); // Parse the XML data
            NodeList readings = weatherDataDoc.getElementsByTagName("time"); // Get all 'time' nodes - each a forecast for a particular time

            LocalDateTime currentTime = LocalDateTime.now();

            for(int i = 0; i < readings.getLength(); i++){ // Iterate through available time nodes to check which covers the correct time window for the current reading
                if(isCorrectReading(currentTime, parseTimeNode(readings.item(i)))) {
                    currentWeather = parseTimeNode(readings.item(i));
                }
            }

            if(currentWeather == null) {
                throw new ForecastNotAvailableException();
            }

            return currentWeather;
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }


    @Override
    public Observable<Weather> getWeatherInHours(int hours) { /* Returns an Observable that provides the weather forecast for a given number of hours in the future, as a stream of Weather objects */
        return Observable.fromCallable(() -> {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document weatherDataDoc = builder.parse(new InputSource(new StringReader(getWeatherXml()))); // Parse the XML data
            NodeList readings = weatherDataDoc.getElementsByTagName("time"); // Get all 'time' nodes - each a forecast for a particular time

            Weather forecast = null;

            LocalDateTime forecastTime = LocalDateTime.now().plusHours((long)hours);

            for(int i = 0; i < readings.getLength(); i++){ // Iterate through available time nodes to check which covers the correct time window for the current reading
                if(isCorrectReading(forecastTime, parseTimeNode(readings.item(i)))) {
                    forecast = parseTimeNode(readings.item(i));
                }
            }

            if(forecast == null) {
                throw new ForecastNotAvailableException();
            }

            return forecast;

        })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<Weather> getWeatherInDays(int days, int timeInHours) { /* Returns an Observable providing the weather forecast for a given number of days in the future and a given number of hours after 12 o'clock */

        // Check in case the request is out of range of available weather data
        if (timeInHours >= 24 || timeInHours <= 0) throw new TimeOutOfRangeException(timeInHours);
        if (days > 5) throw new CrystalBallDepthExceededException(days, timeInHours);
        if (days == 5
                && ((LocalDateTime.now().getHour() / 3) * 3 != timeInHours) // Check that requested time isn't on the boundary of latest available forecast
                && (LocalDateTime.now().getHour() / 3 <= timeInHours / 3)) // Check if requested time is outside the boundaries of the currently available readings (5 days in 3h steps)
            throw new CrystalBallDepthExceededException(days, timeInHours); // Throw exception if requested time is not available via OpenWeatherMap's free API

        if (days == 0
                && ((LocalDateTime.now().getHour() / 3) * 3 + 3 != timeInHours) // Check that requested time isn't on the boundary of latest available forecast
                && (LocalDateTime.now().getHour() / 3 > timeInHours / 3)) // Check if requested time is outside the boundaries of the currently available readings (5 days in 3h steps)
            throw new CrystalBallDepthExceededException(days, timeInHours); // Throw exception if requested time is before the available forecasts (i.e. in the past).

        return Observable.fromCallable(() -> {

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document weatherDataDoc = builder.parse(new InputSource(new StringReader(getWeatherXml()))); // Parse the XML data
            NodeList readings = weatherDataDoc.getElementsByTagName("time"); // Get all 'time' nodes - each a forecast for a particular time

            Weather forecast = null;
            LocalDateTime forecastTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays((long) days).plusHours((long) timeInHours); // Generate LocalDateTime representing arguments

            for(int i = 0; i < readings.getLength(); i++){ // Iterate through available time nodes to check which covers the correct time window for the current reading
                if(isCorrectReading(forecastTime, parseTimeNode(readings.item(i)))) {
                    forecast = parseTimeNode(readings.item(i));
                }
            }

            if(forecast == null) { // Throw exception if the forecast isn't in the list
                throw new ForecastNotAvailableException();
            }

            return forecast;

        })
                .subscribeOn(Schedulers.io());
    }

    private class ForecastNotAvailableException extends RuntimeException{ // Exception thrown if the weather forecast requested is not out of range but no forecast was available.A
        public ForecastNotAvailableException() {
            super("This forecast isn't available, not really sure why. You could be asking for a weird time, or it could be a bug in this code.");
        }
    }

    private class CrystalBallDepthExceededException extends RuntimeException { // Exception thrown if weather forecast time requested is not available via the API
        private final int dayValue;
        private final int timeValue;

        public CrystalBallDepthExceededException(int dayValue, int timeValue){
            super("Requested weather "+dayValue+" days and "+(timeValue - LocalDateTime.now().getHour())+" hours in the future ("+ timeValue+":00).\r\n Sadly, we're not *that* good - we're using the free API and can only forecast until (floor(current time / 3) * 3) o'clock on the 5th day ahead. AND NOT IN THE PAST!");
            this.dayValue = dayValue;
            this.timeValue = timeValue;
        }

        public int getDayValue() {
            return dayValue;
        }

        public int getTimeValue() {
            return timeValue;
        }
    }

    private class TimeOutOfRangeException extends RuntimeException { // Exception thrown if the integer time of day provided to getWeatherInDays is not valid
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

    private boolean isCorrectReading(LocalDateTime currentTime, Weather currentWeather){ /* Small helper function that checks whether the forecasting window represented by the given weather contains the given LocalDateTime
                                                                                            - used while searching for the correct forecast in the weather data. */
        return (currentTime.compareTo(currentWeather.getFromTime()) >= 0 && currentTime.compareTo(currentWeather.getToTime()) <= 0); /* 'from' of the next reading is same as 'to' of the current reading (i.e. the boundary times overlap) but
                                                                                                                                        the iterating-through-readings code will just use the more 'in the future' reading */
    }

    private Weather parseTimeNode(Node timeNode) { // Traverses a single 'time' node (representing a single forecast time), extracting all relevant weather information

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
        for (int i = 0; i < timeAttrs.getLength(); i++) { // Get forecast time window edges
            Node attr = timeAttrs.item(i);
            if (attr.getNodeName().equals("from")) {
                from = LocalDateTime.parse(attr.getNodeValue());
            }
            if (attr.getNodeName().equals("to")) {
                to = LocalDateTime.parse(attr.getNodeValue());
            }
        }

        for (int i = 0; i < timeNode.getChildNodes().getLength(); i++) { // Get weather data
            Node dataNode = timeNode.getChildNodes().item(i);
            if (dataNode.getNodeName().equals("temperature")) {
                currentTemperature = Float.parseFloat(dataNode.getAttributes().getNamedItem("value").getNodeValue());
            }
            if (dataNode.getNodeName().equals("pressure")) {
                currentPressure = Float.parseFloat(dataNode.getAttributes().getNamedItem("value").getNodeValue());
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
            if (dataNode.getNodeName().equals("precipitation")) { /* DISCLAIMER: THERE MAY BE MORE TYPES OF PRECIPITATION. */
                NamedNodeMap attrs = dataNode.getAttributes();

                for (int j = 0; j < attrs.getLength(); j++) { // Precipitation must use a for loop since it may not have a "type" attribute (no precipitation is represented by an empty <precipitation/> tag)
                    Node attr = attrs.item(j);
                    if (attr.getNodeName().equals("type")) {
                        String value = attr.getNodeValue();
                        switch (value) { // Precipitation is represented as an enum so must be converted
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

        currentWind = new Wind(windSpeed, windDir); // Make Wind object (containing wind speed and direction)
        return new Weather(currentPrecipitation, currentCloudCover, currentTemperature, currentPressure, currentHumidity, currentWind, from, to); // return a Weather object containing relevant weather data from the time node
    }

    public static boolean forecastAvailable(int days, int timeInHours){ /* Static method to check whether or not a certain forecast time a given number of days in the future should be available
                                                                            (based on maths and range checks rather than actually checking the XML for the required forecast) */
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
            inRange = false;

        return inRange;
    }
}
