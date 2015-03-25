import java.util.Arrays;
import java.util.List;

/**
 * Created by Mohim on 25/03/2015.
 */
public enum FuelAirConEnum {

    N("Petrol/no AC","0"),
    R("Petrol/AC","2");

    final List<String> details;

    FuelAirConEnum(String text,String score) {
        this.details = Arrays.asList(text, score);
    }

    public String getValue() {
        return details.get(0);
    }

    public String getScore() {
        return details.get(1);
    }

} // FuelAirConEnum


