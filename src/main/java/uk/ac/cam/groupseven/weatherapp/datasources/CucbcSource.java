package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.LightingTimes;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CucbcSource implements RowingInfoSource {
    @Inject
    @Named("cucbcFlagUrl")
    private URL cucbcFlagUrl;
    @Inject
    @Named("cucbcLightingUrl")
    private URL cucbcLightingUrl;

    @Override
    public Observable<FlagStatus> getFlagStatus() {

        return Observable.fromCallable(() -> {
            Document doc = getDocumentFromUrl(cucbcFlagUrl);
            String flagCode = doc.getElementsByTagName("flag").item(0).getAttributes().item(1).getNodeValue();
            return FlagStatus.getFlagFromCode(flagCode);
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }

    @Override
    public Observable<LightingTimes> getLightingStatus() {

        return Observable.fromCallable(() -> {
            Document doc = getDocumentFromUrl(cucbcLightingUrl);
            String todayUp = doc.getElementsByTagName("today").item(0).getAttributes().item(0).getNodeValue();
            String todayDown = doc.getElementsByTagName("today").item(0).getAttributes().item(1).getNodeValue();
            String tomorrowUp = doc.getElementsByTagName("tomorrow").item(0).getAttributes().item(0).getNodeValue();
            String tomorrowDown = doc.getElementsByTagName("tomorrow").item(0).getAttributes().item(1).getNodeValue();
            return new LightingTimes(todayUp, todayDown, tomorrowUp, tomorrowDown);
        })
                .subscribeOn(Schedulers.io()); //Do in background io thread

    }

    private Document getDocumentFromUrl(URL url) throws IOException, ParserConfigurationException, SAXException {
        URLConnection con = url.openConnection();
        try (InputStream inputStream = con.getInputStream()) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(inputStream);
        }
    }
}
