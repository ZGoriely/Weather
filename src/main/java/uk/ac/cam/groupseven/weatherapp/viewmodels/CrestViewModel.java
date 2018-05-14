package uk.ac.cam.groupseven.weatherapp.viewmodels;

import uk.ac.cam.groupseven.weatherapp.models.Crest;

import javax.swing.*;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

public class CrestViewModel {

    public final Throwable error;
    public final TreeMap<String, ImageIcon> images;

    public CrestViewModel(String imageDirectory) {
        File[] imageFiles = new File(imageDirectory).listFiles();
        List<Crest> crests = new LinkedList<Crest>();
        this.images = new TreeMap<String, ImageIcon>();
        for (File image : imageFiles) {
            for (Crest crest : Crest.values()) {
                String name = image.getName().substring(0, image.getName().lastIndexOf("."));
                if (name.equals(crest.getCode())) {
                    images.put(name, new ImageIcon(image.getPath()));
                }
            }
        }
        error = null;
    }

    private CrestViewModel() {
        this.images = null;
        error = null;
    }

    public CrestViewModel(Throwable throwable) {
        this.images = null;
        error = throwable;
    }


}
