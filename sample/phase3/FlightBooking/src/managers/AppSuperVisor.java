package managers;

import java.util.*;
import data.*;
import android.app.Application;
import android.content.Context;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import android.content.ContextWrapper;

/**
 * A class representing a supervisor of this flight booking application.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class AppSuperVisor extends Application {

    /** MainSystem of this flight booking application. */
    private MainSystem system = new MainSystem();

    /** Client who is controlled by administrator. */
    private Client clientControlled = null;

    /** A variable that passes the search object to another activity. */
    private Search searchObj;

    /** The name of the directory in internal storage. */
    private String filepath = "data_storage";

    /** The name of the file that is used to save client info. */
    private String clientFileName = "clients.csv";

    /** The name of the file that is used to save flight info. */
    private String flightFileName = "flights.csv";

    /** The name of the file that is used to save itinerary info. */
    private String itienraryFileName = "itineraries.csv";

    /** The name of the file that is used to save booked itineraries. */
    private String bookedFileName = "booked.csv";

    /**
     * The name of the file that is used to save the list
     * of itineraries inside the shopping cart.
     */
    private String cartFileName = "shopping_cart.csv";

    /**
     * The name of the file that is used to save the list of
     * passengers.
     */
    private String passengersFileName = "passengers.csv";

    /**
     * The name of the file that is used to save the list of flights
     * required to complete each itinerary.
     */
    private String flightRouteFileName = "flight_route.csv";

    /**
     * Returns the MainSystem of this flight booking application.
     * @return the MainSystem of this flight booking application
     */
    public MainSystem getMainSystem() {
        return system;
    }

    /**
     * Returns true if the admin hasn't decided who to control.
     * @return true if the admin hasn't decided who to control
     */
    public boolean noClientControlled() {
        return clientControlled == null;
    }

    /**
     * Returns the client who is currently controlled by admin.
     * @return the client who is currently controlled by admin
     */
    public Client getClientControlled() {
        return clientControlled;
    }

    /**
     * Sets the client the admin wants to control to c
     * @param c the Client who will be controlled by admin
     */
    public void setClientControlled(Client c) {
        clientControlled = c;
    }

    /**
     * Sets the search object to searchObj.
     * This method is for passing the search object from
     * one activity to another, in android app.
     * @param searchObj the search object that needs to be
     * passed to another activity
     */
    public void setSearchObject(Search searchObj) {
        this.searchObj = searchObj;
    }

    /**
     * Returns the search object that has been passed from
     * the previous activity in android app.
     * @return the search object that has been passed from
     * the previous activity
     */
    public Search getSearchObject() {
        return this.searchObj;
    }

    /**
     * Uploads the data inside the files located in internal storage
     * to the flight booking application.
     * @param mode the type of upload (option: client/flight/itinerary)
     * @param filename the filename that contains all the data
     * @throws IOException if we don't have such file
     */
    private void uploadData(int mode, String filename)
            throws IOException {
        ContextWrapper contextWrapper = new ContextWrapper(
                getApplicationContext());
        File directory = contextWrapper.getDir(
                filepath, Context.MODE_PRIVATE);
        File myInternalFile = new File(directory, filename);
        String path = myInternalFile.getPath();
        FileReader myfile = new FileReader(path);
        BufferedReader br = new BufferedReader(myfile);
        String line;
        while ((line = br.readLine()) != null && line != "") {
            String tmpline = line.replace("\n", "");
            String[] s = tmpline.split(",");
            try {
                if (mode == 0) {
                    Client c = new Client(
                            s[3], s[2], s[0], s[6], s[4], s[5]);
                    c.setPassword(s[1]);
                    system.addClientToDatabase(c);
                }
                if (mode == 1) {
                    Flight f = new Flight(
                            s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7]);
                    system.addFlightToDatabase(f);
                }
                if (mode == 2) {
                    Itinerary e = new Itinerary(s[0]);
                    system.addItineraryToDatabase(e);
                }
            } catch (InvalidTransactionException e) {
                System.out.println(e.getMessage());
            }
        }
        br.close();
    }

    /**
     * Connects the main data to sub data.
     * For example, if the client has already booked several itineraries
     * before he closes the app, then we need to add these itineraries
     * to this Client's account when he relaunches the app.
     * @param mode the type of upload.
     * (options: bookedItinerary/shoppingCart/passengers/flight_route)
     * @param filename the filename that contains all the data
     * @throws IOException if we couldn't find a file to open
     */
    private void connectData(int mode, String filename)
            throws IOException {
        ContextWrapper contextWrapper = new ContextWrapper(
                getApplicationContext());
        File directory = contextWrapper.getDir(
                filepath, Context.MODE_PRIVATE);
        File myInternalFile = new File(directory, filename);
        String path = myInternalFile.getPath();
        FileReader myfile = new FileReader(path);
        BufferedReader br = new BufferedReader(myfile);
        String line;
        while ((line = br.readLine()) != null && line != "") {
            String tmpline = line.replace("\n", "");
            String[] s = tmpline.split(",");
            try {
                if (mode == 0 || mode == 1) {
                    Client c = system.getClient(s[0]);
                    for (int i = 1; i < s.length; i++) {
                        if (mode == 0) {
                            c.bookItinerary(system.getItinerary(s[i]));
                        }
                        if (mode == 1) {
                            Itinerary ee = system.getItinerary(s[i]);
                            c.addItineraryToShoppingCart(ee);
                        }
                    }
                }
                if (mode == 2) {
                    Flight f = system.getFlight(s[0]);
                    for (int i = 1; i < s.length; i++)
                        f.addClient(system.getClient(s[i]));
                }
                if (mode == 3) {
                    Itinerary e = system.getItinerary(s[0]);
                    for (int i = 1; i < s.length; i++)
                        e.addFlight(system.getFlight(s[i]));
                }
            } catch (InvalidTransactionException e) {
                System.out.println(e.getMessage());
            }
        }
        br.close();
    }

    /**
     * Uploads all the data inside the file located in internal storage to
     * flight booking application.
     * @throws IOException if we couldn't find a file we need to open
     */
    public void uploadPermanentData() throws IOException {

        uploadData(0, clientFileName); // uploading clients
        uploadData(1, flightFileName); // uploading flights
        uploadData(2, itienraryFileName); // uploading itieneraries

        // connects client to booked itineraries.
        connectData(0, bookedFileName);

        // connects client to his shopping cart
        connectData(1, cartFileName);

        // connects flight to its passengers.
        connectData(2, passengersFileName);

        // connects itinerary to its flights.
        connectData(3, flightRouteFileName);
    }

    /**
     * Saves the data into internal storage when the app closed.
     * 
     * FORMAT OF FILE:
     * ~~~~~~~~~~~~~~~
     * - clients.csv:
     * [email,password,firstname,lastname,creditCard#,expiryDate,address]
     * - booked.csv
     * [email,itinerary#1,itinerary#2,...]
     * - shopping_cart.csv
     * [email,itinerary#1,itinerary#2,...]
     * 
     * - flights.csv
     * [flight#,deptDateTime,arrivalDateTime,airline,origin,dest,cost,seat#]
     * - passengers.csv [flight#,email1,email2,...]
     * 
     * - itineraries.csv
     * [iNumber]
     * - flight_route.csv
     * [itinerary#,flight#1,flight#2,...]
     */
    public void savePermanentData()
            throws FileNotFoundException, IOException {
        HashMap<String, Client> clients = system.getClientDatabase();
        HashMap<String, Flight> flights = system.getFlightDatabase();
        HashMap<String, Itinerary> es = system.getItineraryDatabase();
        String clientStr = "", flightStr = "", itinerarieStr = "";
        String bookedStr = "", shoppingCartStr = "", passengerStr = "";
        String flightRouteStr = "";

        for (String key : clients.keySet()) {
            Client c = clients.get(key);
            clientStr += c.getStringRepr() + "\n";
            TreeSet<Itinerary> booked = c.getBookedItineraries();
            TreeSet<Itinerary> shopingCart = c.getShoppingCart();
            if (booked.size() > 0) {
                bookedStr += c.getEmail() + ",";
                for (Itinerary e : booked)
                    bookedStr += e.getItineraryNumber() + ",";
                bookedStr = bookedStr.substring(0, bookedStr.length() - 1);
                bookedStr += "\n";
            }
            if (shopingCart.size() > 0) {
                shoppingCartStr += c.getEmail() + ",";
                for (Itinerary e : shopingCart)
                    shoppingCartStr += e.getItineraryNumber() + ",";
                shoppingCartStr = shoppingCartStr.substring(
                        0, shoppingCartStr.length() - 1);
                shoppingCartStr += "\n";
            }
        }
        for (String key : flights.keySet()) {
            Flight f = flights.get(key);
            flightStr += f.getStringRepr() + '\n';
            TreeSet<Client> passengers = f.getPassengers();
            if (passengers.size() > 0) {
                passengerStr += f.getFlightNumber() + ",";
                for (Client c : passengers)
                    passengerStr += c.getEmail() + ",";
                passengerStr = passengerStr.substring(
                        0, passengerStr.length() - 1);
                passengerStr += "\n";
            }
        }
        for (String key : es.keySet()) {
            Itinerary e = es.get(key);
            itinerarieStr += e.getItineraryNumber() + '\n';
            TreeSet<Flight> flightRoute = e.getFlights();
            if (flightRoute.size() > 0) {
                flightRouteStr += e.getItineraryNumber() + ",";
                for (Flight f : flightRoute)
                    flightRouteStr += f.getFlightNumber() + ",";
                flightRouteStr = flightRouteStr.substring(
                        0, flightRouteStr.length() - 1);
                flightRouteStr += "\n";
            }
        }

        String[] filenames = {
                clientFileName, bookedFileName, cartFileName,
                flightFileName, passengersFileName, itienraryFileName,
                flightRouteFileName };

        String[] strs = {
                clientStr, bookedStr, shoppingCartStr, flightStr,
                passengerStr, itinerarieStr, flightRouteStr };

        ContextWrapper contextWrapper = new ContextWrapper(
                getApplicationContext());
        File directory = contextWrapper.getDir(
                filepath, Context.MODE_PRIVATE);
        for (int i = 0; i < strs.length; i++) {
            File myInternalFile = new File(directory, filenames[i]);
            FileOutputStream fos = new FileOutputStream(myInternalFile);
            fos.write(strs[i].getBytes());
            fos.close();
        }

    }
}
