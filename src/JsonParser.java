import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;

/**
 * This java console application parses the a json file and
 * outputs the contents in the specified formats.
 * <p/>
 * 1. •	Print a list of all the cars, in ascending price order,
 * in the following format: 1. {Vehicle name} – {Price}
 * <p/>
 * 2. •	Using the table below, calculate the specification of the
 * vehicles based on their SIPP. Print the specification out using
 * the following format:
 * 1. {Vehicle name} – {SIPP} – {Car type} – {Car type/doors} –
 * {Transmission} – {Fuel} – {Air con}
 * <p/>
 * <p/>
 * 3. •	Print out the highest rated supplier per car type, descending
 * order, in the following format:
 * 1. {Vehicle name} – {Car type} – {Supplier} – {Rating}
 * <p/>
 * 4. •	Give each vehicle a score based on the below breakdown, then
 * combine this score with the suppliers rating. Print out a list of
 * vehicles, ordered by the sum of the scores in descending order, in
 * the following format:
 * 1.	{Vehicle name} – {Vehicle score} – {Supplier rating} – {Sum of scores}
 */
public class JsonParser {

    /**
     * Holds an arrayList of all rentalVehicles found from JSON.
     */
    private static ArrayList<RentalVehicle> rentalVehicles = new ArrayList<RentalVehicle>();

    /**
     * @param args takes the filename of the json file to parse
     */
    public static void main(String[] args) {

        try {

            // Validation checking if file entered
            if (args.length < 1) { throw new FileNotFoundException(); }

            // Store the file name entered by user
            String jsonFileName = args[0];

            System.out.println("Reading file: " + jsonFileName);
            FileReader fileReader = new FileReader(jsonFileName);
            JSONParser jsonParser = new JSONParser();
            // Also validates json here, throws ParseException if invalid.
            JSONObject jsonOriginalObject = (JSONObject) jsonParser.parse(fileReader);

            JSONObject search = (JSONObject) jsonOriginalObject.get("Search");
            JSONArray vehicleList = (JSONArray) search.get("VehicleList");

            //Loop through each vehicle object saving it.
            for (int i = 0; i < vehicleList.size(); i++) {
                JSONObject currentObject = (JSONObject) vehicleList.get(i);
                RentalVehicle currentRentalVehicle = createNewRentalVehicle(currentObject);
                rentalVehicles.add(currentRentalVehicle);
            }

            printListPriceAsc(); // Print in asc order of price
            calculateSpecBasedOnSipp(); // Calculates spec from config files.
            printHighestRatedSupplierPerCarType();
            printVehiclesOrderedByScore();

        } //try
        catch (FileNotFoundException e) {
            System.out.println("File not found. Enter a correct "
                    + "json filename. e.g vehicles.json ");
        } catch (ParseException e) {
            // Any errors in parsing let the user know.
            System.out.println(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // Any other errors let the user know
            System.out.println(e.toString());
        }

    } // main method


    /**
     * Prints rental cars in asc order of price
     */
    private static void printListPriceAsc() {
        System.out.println("Prints rental cars in asc order of price \n");
        Collections.sort(rentalVehicles, new OrderByPriceAsc());

        for (int i = 0; i < rentalVehicles.size(); i++) {
            String string = (i + 1) + ".\t" + rentalVehicles.get(i).getName()
                    + " - " + rentalVehicles.get(i).getPrice();
            System.out.println(string);
        }
        System.out.println();
    }

    /**
     * Calculates the specification based on Sipp
     */
    private static void calculateSpecBasedOnSipp() {
        System.out.println("Calculate the specification of the vehicles based"
                + " on their SIPP. Assumption, if letter from json doesn't"
                + " exist in enum  list, then it wont be printed out.");

        for (int i = 0; i < rentalVehicles.size(); i++) {
            RentalVehicle currentRV = rentalVehicles.get(i);

            StringBuffer spec = new StringBuffer();
            spec.append(currentRV.getName() + " - " + currentRV.getSipp());
            String sipp = rentalVehicles.get(i).getSipp();
            char sippArray[] = sipp.toCharArray();

            // Index 0 EnumCarType Enum
            spec.append(" - " + getEnumCarType(sippArray[0]));

            // Index 1 EnumDoorsCarType Enum
            spec.append(" - " + getEnumDoorsCarType(sippArray[1]));

            // Index 2 EnumTransmission Enum
            spec.append(" - " + getEnumTransmission(sippArray[2]));

            // Index 3 EnumFuelAirCon Enum
            spec.append(" - " + getEnumFuelAirCon(sippArray[3]));

            // {Vehicle name} – {SIPP} – {Car type}–{ Car type/doors} –{Transmission} – {Fuel} – {Air con}
            System.out.println((i + 1) + ".\t" + spec.toString());

        } // for
        System.out.println();
    } // calculateSpecBasedOnSipp

    /**
     * Print out the highest rated supplier per car type, descending order
     */
    private static void printHighestRatedSupplierPerCarType() {
        System.out.println("Print out the highest rated supplier per car type"
                + " desc. Assumption if CarType doesn't exit, then it wont be"
                + " printed");

        Collections.sort(rentalVehicles, new OrderByRatingDesc());

        /* Outer for loop we want to loop through all car types,Inner for loop
           pick the first one from rentalVehicles matchingCar Type
        */
        int index = 1;

        for (CarTypeEnum carTypeEnum : EnumSet.allOf(CarTypeEnum.class)) {

            for (int i = 0; i < rentalVehicles.size(); i++) {

                RentalVehicle currentVehicle = rentalVehicles.get(i);
                String sipp = currentVehicle.getSipp();
                char sippArray[] = sipp.toCharArray();
                String carType = getEnumCarType(sippArray[0]);

                if (carType.equals("" + carTypeEnum.getValue())) {
                    // 1. {Vehicle name} – {Car type} – {Supplier} – {Rating}
                    String string = index + ".\t" + currentVehicle.getName()
                            + " - " + carType + " - " + currentVehicle.getSupplier()
                            + " - " + currentVehicle.getRating();
                    System.out.println(string);
                    index++;
                    break;
                }
            } // inner for loop

        } // outer for loop

        System.out.println();
    } // printHighestRatedSupplierPerCarType

    /**
     * Print out a list of vehicles, ordered by the sum
     * of the scores in desc order.
     */
    private static void printVehiclesOrderedByScore() {
        System.out.println("Print out a list of vehicles, ordered by the sum"
                + " of the scores in desc order");

        Collections.sort(rentalVehicles, new OrderByScoreDesc());

        for (int i = 0; i < rentalVehicles.size(); i++) {

            RentalVehicle currentRV = rentalVehicles.get(i);

            //1.{Vehicle name} – {Vehicle score} – {Supplier rating}
            // – {Sum of scores}
            String string = currentRV.getName() + " - "
                    + currentRV.getScore() + " - " + currentRV.getRating()
                    + " - " + currentRV.getTotalScore();
            System.out.println("" + (i + 1) + ".\t" + string);
        } // for

        System.out.println();

    } // printVehiclesOrderedByScore

    private static String getEnumCarType(char myEnum) {
        for (CarTypeEnum carType : CarTypeEnum.values()) {
            if (carType.toString().equals("" + myEnum)) {
                return carType.getValue();
            }
        }
        return "";
    } // getEnumCarType


    private static String getEnumDoorsCarType(char myEnum) {
        for (DoorsCarTypeEnum carType : DoorsCarTypeEnum.values()) {
            if (carType.toString().equals("" + myEnum)) {
                return carType.getValue();
            }
        }
        return "";
    } // getEnumDoorsCarType


    private static String getEnumTransmission(char myEnum) {
        for (TransmissionEnum carType : TransmissionEnum.values()) {
            if (carType.toString().equals("" + myEnum)) {
                return carType.getValue();
            }
        }
        return "";
    } // getEnumTransmission

    private static double getEnumTransmissionScore(char myEnum) {
        for (TransmissionEnum carType : TransmissionEnum.values()) {
            if (carType.toString().equals("" + myEnum)) {
                return Integer.parseInt(carType.getScore());
            }
        }
        return 0 ;
    } // getEnumTransmissionScore

    private static double getEnumAirConScore(char myEnum) {
        for (FuelAirConEnum carType : FuelAirConEnum.values()) {
            if (carType.toString().equals("" + myEnum)) {
                return Integer.parseInt(carType.getScore());

            }
        }
        return 0;
    } // getEnumFuelAirCon

    private static String getEnumFuelAirCon(char myEnum) {
        for (FuelAirConEnum carType : FuelAirConEnum.values()) {
            if (carType.toString().equals("" + myEnum)) {
                String split[] = carType.getValue().split("/");
                return split[0] + " - " + split[1];
            }
        }
        return "";
    } // getEnumFuelAirCon

    /**
     * @param myJsonObject object holding each rental car in json format
     * @return returns a new RentalCar object
     */
    private static RentalVehicle createNewRentalVehicle(JSONObject myJsonObject) {
        // Get information from JSON object
        String sipp = myJsonObject.get("sipp").toString();
        String name = myJsonObject.get("name").toString();
        double price = Double.parseDouble(myJsonObject.get("price").toString());
        String supplier = myJsonObject.get("supplier").toString();
        double rating = Double.parseDouble(myJsonObject.get("rating").toString());

        // Create new RentalCar Object
        RentalVehicle newRentalVehicle = new RentalVehicle(sipp, name, price, supplier, rating);
        // Calculate score based on transmission and air con
        double score = calculateScore(newRentalVehicle);
        newRentalVehicle.updateTotalScore(score);

        return newRentalVehicle;
    } // createNewRentalVehicle

    /**
     * @param newRentalVehicle vehicle to calculate score from
     * @return returns a score based on transmission and air con
     */
    private static double calculateScore(RentalVehicle newRentalVehicle) {

        double scoreSoFar = 0;
        String sipp = newRentalVehicle.getSipp();
        char sippArray[] = sipp.toCharArray();

        scoreSoFar += getEnumTransmissionScore(sippArray[2]);
        scoreSoFar += getEnumAirConScore(sippArray[3]);

        return scoreSoFar;
    }


    /**
     * This method orders by price asc
     */
    private static class OrderByPriceAsc implements Comparator<RentalVehicle> {

        @Override
        public int compare(RentalVehicle a, RentalVehicle b) {
            if (a.getPrice() == b.getPrice()) {
                return 0;
            }
            if (a.getPrice() < b.getPrice()) {
                return -1; //Indicates we want a first
            } else {
                return 1; // Indicates we want b first
            }
        } // compare
    } // OrderByPriceAsc

    /**
     * This method orders by rating desc
     */
    private static class OrderByRatingDesc implements Comparator<RentalVehicle> {

        @Override
        public int compare(RentalVehicle a, RentalVehicle b) {
            if (a.getRating() == b.getRating()) {
                return 0;
            }
            if (a.getRating() > b.getRating()) {
                return -1; //Indicates we want a first
            } else {
                return 1; // Indicates we want b first
            }
        } // compare
    } // OrderByRatingDesc

    /**
     * This method orders by Score desc
     */
    private static class OrderByScoreDesc implements Comparator<RentalVehicle> {

        @Override
        public int compare(RentalVehicle a, RentalVehicle b) {
            if (a.getTotalScore() == b.getTotalScore()) {
                return 0;
            }
            if (a.getTotalScore() > b.getTotalScore()) {
                return -1; //Indicates we want a first
            } else {
                return 1; // Indicates we want b first
            }
        } // compare
    } // OrderByScoreDesc

} // JsonParser class
