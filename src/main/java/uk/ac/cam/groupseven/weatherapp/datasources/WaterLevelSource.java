package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import uk.ac.cam.groupseven.weatherapp.models.WaterLevel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class WaterLevelSource {
    @Inject
    @Named("waterLevelSourceUrl")
    private URL waterLevelSourceUrl;

    public Observable<WaterLevel> getWaterLevelNow() {
        return Observable.fromCallable(() -> {


            URLConnection con = waterLevelSourceUrl.openConnection();
            InputStream instream = con.getInputStream();

            // This code is gonna look really ugly, but it's simpler than importing another
            // library just to read in one json file

            BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            String newline = reader.readLine();
            while (newline != null) {
                if (newline.contains("\"value\" : ")) {
                    break;
                }
                newline = reader.readLine();
            }

            String valueString = newline.substring(newline.lastIndexOf(":") + 2);
            float waterLevel = Float.parseFloat(valueString);
            return new WaterLevel(waterLevel);
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }
}
