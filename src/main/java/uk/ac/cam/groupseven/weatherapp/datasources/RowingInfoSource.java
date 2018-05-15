package uk.ac.cam.groupseven.weatherapp.datasources;

import com.google.inject.ImplementedBy;
import io.reactivex.Observable;
import uk.ac.cam.groupseven.weatherapp.models.FlagStatus;
import uk.ac.cam.groupseven.weatherapp.models.LightingTimes;

@ImplementedBy(CucbcSource.class)
public interface RowingInfoSource {
    Observable<FlagStatus> getFlagStatus();

    Observable<LightingTimes> getLightingStatus();
}
