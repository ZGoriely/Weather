package uk.ac.cam.groupseven.weatherapp.models;

public enum FlagStatus {

    GREEN("Green", "grn"),
    YELLOW("Yellow", "yel"),
    REDYELLOW("Red/Yellow", "ryl"),
    NONOPERATIONAL("Not Operational", "nop"),
    BLUE("Cambridge Blue", "blu"),
    GDBO("GDBO", "gdb"),
    RED("Red", "red");

    private final String displayName;
    private final String code;

    FlagStatus(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    public static FlagStatus getFlagFromCode(String code) {
        for (FlagStatus status : FlagStatus.values()) {
            if (code.toLowerCase().equals(status.getCode()))
                return status;
        }
        return FlagStatus.NONOPERATIONAL;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }
}
