package uk.ac.cam.groupseven.weatherapp.viewmodels;

import java.nio.file.Path;
import java.util.TreeMap;

public class CrestViewModel {

    public final Throwable error;
    public final TreeMap<String, Path> crests;

    public CrestViewModel(TreeMap<String, Path> crests) {
        this.crests = crests;
        error = null;
    }

    private CrestViewModel() {
        this.crests = null;
        error = null;
    }

    public CrestViewModel(Throwable throwable) {
        this.crests = null;
        error = throwable;
    }


}
