import java.util.Currency;

/**
 * Created by Mohim on 24/03/2015.
 * This class holds the structure for each Vehicle object
 * so that it can be used without referring to the JSON array each time
 */
public class RentalVehicle {

    private String sipp = "";
    private String name = "";
    private double price;
    private String supplier = "";
    private double rating;
    private double totalScore = 0;
    private double score;

    public RentalVehicle(String sipp, String name, Double price,
                         String supplier, double rating) {

        this.sipp = sipp;
        this.name = name;
        this.price = price;
        this.supplier = supplier;
        this.rating = rating;
        this.score = 0;
        this.totalScore = rating;
    }

    public String getSipp() {
        return sipp;
    } // getSipp

    public String getName() {
        return name;
    } // getName

    public double getPrice() {
        return price;
    } // getPrice

    public String getSupplier() {
        return supplier;
    } // getSupplier

    public double getRating() {
        return rating;
    } // getRating

    public double getScore() {
        return score;
    } // getScore

    public double getTotalScore() {
        return totalScore;
    } // getTotalScore

    public void updateTotalScore(double vehicleScore) {
        score = vehicleScore;
        totalScore = score + rating;
    } // updateTotalScore

    public String toString() {
        return "sipp: " + sipp + " price: " + price + " supplier: " + supplier
                + " name: " + name + " rating: " + rating
                + " totalScore: " + totalScore;
    }


}
