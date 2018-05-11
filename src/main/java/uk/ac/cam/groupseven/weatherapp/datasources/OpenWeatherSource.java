package uk.ac.cam.groupseven.weatherapp.datasources;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.cam.groupseven.weatherapp.models.Weather;

import javax.xml.bind.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class OpenWeatherSource {

    URL apiUrl;
    InputStream apiResponse;

    public Observable<Weather> getWeatherNow() {
        try {
            apiUrl = new URL("http://api.openweathermap.org/data/2.5/forecast?APPID=8b35f0643a9e43fac171d05738bd2b8d&id=2653941&units=metric&mode=xml"); // TODO Get Nathan to @Inject this (and maybe tidy up and separate url parameters)
        }
        catch(MalformedURLException e) {
            System.out.println(e.getMessage());
        }


        return Observable.fromCallable(() -> {
            try {
                apiResponse = apiUrl.openStream();
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document weatherDataDoc = builder.parse(apiResponse);
                NodeList readings = weatherDataDoc.getElementsByTagName("time");

                for(int i = 0; i < readings.item(0).getChildNodes().getLength(); i++){
                    Node dataNode = readings.item(0).getChildNodes().item(i);
                    if(dataNode.getNodeName().equals("temperature")) {
                        System.out.println("Temperature: " + dataNode.getAttributes().getNamedItem("value").getNodeValue());
                    }
                    if(dataNode.getNodeName().equals("windDirection")){
                        System.out.println("Wind Direction: "+dataNode.getAttributes().getNamedItem("code").getNodeValue());
                    }
                    if(dataNode.getNodeName().equals("windSpeed")){
                        System.out.println("Wind Speed: "+dataNode.getAttributes().getNamedItem("mps").getNodeValue()+", "+dataNode.getAttributes().getNamedItem("name").getNodeValue());
                    }
                    if(dataNode.getNodeName().equals("clouds")){
                        System.out.println("Cloud cover: "+dataNode.getAttributes().getNamedItem("all").getNodeValue()+"%");
                    }
                }

                //TODO - Potential issues - what happens if data not in tags? (e.g. with precipitation)
            }
            catch(IOException i) {
                System.out.println(i.getMessage());
            }
            //TODO: Replace with API call
            Thread.sleep(1000); //Pretend to do some work;
            return new Weather(Weather.Precipitation.NONE);
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }

    public Observable<Weather> getWeatherInHours(int hours) {
        return Observable.fromCallable(() -> {
            //TODO: Replace with API call
            Thread.sleep(1000); //Pretend to do some work;
            return new Weather(Weather.Precipitation.values()[hours % 3]); //Fast and accurate weather prediction
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }

}
