/**
 * Created by Mohim on 25/03/2015.
 */
public enum DoorsCarTypeEnum {

    B("2 doors"),
    C("4 doors"),
    D("5 doors"),
    W("Estate"),
    T("Convertible"),
    F("SUV"),
    P("Pick up"),
    V("Passenger Van");

    private final String details;

    private DoorsCarTypeEnum(String details) {
        this.details = details;
    }

    public String getValue() {
        return details;
    }

} // DoorsCarTypeEnum


