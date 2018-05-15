package uk.ac.cam.groupseven.weatherapp.datasources;


import com.google.inject.Inject;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.cam.groupseven.weatherapp.models.Weather;
import uk.ac.cam.groupseven.weatherapp.models.Wind;

import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;

public class OpenWeatherSource implements WeatherSource {

    @Inject
    @Named("openWeatherApiUrl")
    private URL apiUrl;
    private InputStream apiResponse;

    @Override
    public Observable<Weather> getWeatherNow() {

        /* There are a lot of for loops as the XML parsing system seems to create a lot of NodeLists, which you can only iterate through */
        return Observable.fromCallable(() -> {
            Weather.Precipitation currentPrecipitation = Weather.Precipitation.NONE;
            Integer currentCloudCover = null;
            Float currentTemperature = null;
            Wind currentWind;
            Float windSpeed = null;
            String windDir = null;
            apiResponse = apiUrl.openStream();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document weatherDataDoc = builder.parse(apiResponse);
            NodeList readings = weatherDataDoc.getElementsByTagName("time");


            for (int i = 0; i < readings.item(0).getChildNodes().getLength(); i++) { /* Get first forecast in response (current-ish) */
                Node dataNode = readings.item(0).getChildNodes().item(i);
                if (dataNode.getNodeName().equals("temperature")) {
                    currentTemperature = Float.parseFloat(dataNode.getAttributes().getNamedItem("value").getNodeValue()); /* TODO Careful of parsing floats etc */
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
            return new Weather(currentPrecipitation, currentCloudCover, currentTemperature, currentWind);
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }

    //TODO: Implement methods for getting future weather
    @Override
    public Observable<Weather> getWeatherInHours(int hours) {
        throw new NotImplementedException();

    }

    private class UnsupportedPrecipitationException extends RuntimeException {
        private final String value;

        UnsupportedPrecipitationException(String value) {

            this.value = value;
        }
    }
}
