/**
 * Created by Mohim on 25/03/2015.
 */
public enum CarTypeEnum {

    M("Mini"),
    E("Economy"),
    C("Compact"),
    I("Intermediate"),
    S("Standard"),
    F("Full size"),
    P("Premium"),
    L("Luxury"),
    X("Special");
    private final String details;

    private CarTypeEnum(String details) {
        this.details = details;
    }

    public String getValue() {
        return details;
    }

} // CarTypeEnum


