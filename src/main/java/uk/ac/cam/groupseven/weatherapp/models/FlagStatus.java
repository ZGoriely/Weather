package uk.ac.cam.groupseven.weatherapp.models;

public class FlagStatus {
    public final Status status;

    public FlagStatus(Status status) {
        this.status = status;
    }

    public enum Status {GREEN, RED}

}
