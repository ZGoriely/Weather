package uk.ac.cam.groupseven.weatherapp.viewmodels;

import javax.swing.*;
import java.util.TreeMap;

public class CrestViewModel {

    public final Throwable error;
    public final TreeMap<String, ImageIcon> images;

    public CrestViewModel(TreeMap<String, ImageIcon> images) {
        this.images = images;

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
