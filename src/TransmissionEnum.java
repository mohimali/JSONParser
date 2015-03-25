import java.util.Arrays;
import java.util.List;

/**
 * Created by Mohim on 25/03/2015.
 */
public enum TransmissionEnum {

    M("Manual","1"),
    A("Automatic","5");
    final List<String> details;

     TransmissionEnum(String text,String score) {
        this.details = Arrays.asList(text,score);
    }

    public String getValue() {
        return details.get(0);
    }

    public String getScore() {
        return details.get(1);
    }

} // TransmissionEnum


