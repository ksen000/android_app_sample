package managers;

import java.util.*;
import data.*;

/**
 * A class representing a MainSystem of this Flight booking application.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class MainSystem {

    /** The type of user currently logged into this MainSystem. */
    private String userType;

    /** The database of Itinerary. */
    private HashMap<String, Itinerary> itineraryDatabase;

    /** The database of Client. */
    private HashMap<String, Client> clientDatabase;

    /** The database of Flight. */
    private HashMap<String, Flight> flightDatabase;

    /**
     * Creates a new MainSystem with the given type of user.
     * @param userType the type of user currently logged into
     * this MainSystem.
     */
    public MainSystem(String userType) {

        // Email address if client, or 'admin' if admin.
        this.userType = userType;
        itineraryDatabase = new HashMap<String, Itinerary>();
        clientDatabase = new HashMap<String, Client>();
        flightDatabase = new HashMap<String, Flight>();
    }

    /**
     * Creates a new MainSystem.
     */
    public MainSystem() {
        itineraryDatabase = new HashMap<String, Itinerary>();
        clientDatabase = new HashMap<String, Client>();
        flightDatabase = new HashMap<String, Flight>();
    }

    /**
     * Returns 1 if the login is valid. Returns -1 if the login is invalid.
     * @param email the email address of the user who is trying to login
     * @param password the password of the user who is trying to login
     * @return 1 if login is valid else -1
     */
    public int checkLogin(String email, String password)
            throws InvalidTransactionException {

        // email: admin with password: b07 is allowed to login.
        if (email.equals("admin") && password.equals("b07")) {
            return 1;
        }

        // -1 if we find a Client but the password is wrong
        // or the Client hasn't registered yet.
        // 1 if we successfully find a Client whose email is email.
        if (!containsClient(email)) {
            return -1;
        }
        Client c = getClient(email);
        if (!c.getPassword().equals(password)) {
            return -1;
        }
        return 1;
    }

    /**
     * Returns the type of user currently logged in to this flight booking
     * application.
     * @return the user type of the user who is currently logged
     * into this flight booking system
     */
    public String getUserType() {
        return userType;
    }

    /**
     * Sets the type of user currently logged in to this flight booking
     * application to userType.
     * @param userType the type of user currently logged in to this
     * flight booking application
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * Sets the type of user currently logged into this flight booking
     * application to none. Called when the user logs out from the app.
     */
    public void discardUserType() {
        this.userType = "";
    }

    /**
     * Returns true if the itinerary database contains the Itinerary s.t
     * itinerary # == itineraryNum
     * @param itineraryNum itinerary # of wanted Itinerary
     * @return true if the itinerary database contains the wanted Itinerary
     */
    public boolean containsItinerary(String itineraryNum) {
        return itineraryDatabase.containsKey(itineraryNum);
    }

    /**
     * Returns the wanted Itinerary
     * @param itineraryNumber the itinerary # of wanted Itinerary
     * @return the wanted Itinerary
     * @throws InvalidTransactionException
     */
    public Itinerary getItinerary(String itineraryNumber)
            throws InvalidTransactionException {
        if (!containsItinerary(itineraryNumber)) {
            String msg = "No such Flight @ getItinerary";
            throw new InvalidTransactionException(msg);
        }
        Itinerary e = itineraryDatabase.get(itineraryNumber);
        e.setAllItineraryInfo();
        return e;
    }

    /**
     * Returns the list of itineraries stored in this flight booking app.
     * @return the list of itineraries in this flight booking app
     */
    public HashMap<String, Itinerary> getItineraryDatabase() {
        return itineraryDatabase;
    }

    /**
     * Adds the given Itinerary to Itinerary database.
     * @param e the Itinerary to be added into database
     * @throws InvalidTransactionException
     *             if the database already has the same Itinerary
     */
    public void addItineraryToDatabase(Itinerary e)
            throws InvalidTransactionException {
        if (containsItinerary(e.getItineraryNumber())) {
            String message = "Itinerary Number is alraedy taken ";
            message += "@ addItineraryToDatabase";
            throw new InvalidTransactionException(message);
        }
        e.setAllItineraryInfo();
        itineraryDatabase.put(e.getItineraryNumber(), e);
    }

    /**
     * Deletes the Itinerary from Itinerary database.
     * @param e the Itinerary to be deleted from database
     * @throws InvalidTransactionException
     * if the Client attempted to delete the Itinerary, or database
     * don't have the Itinerary we want to delete
     */
    public void deleteItineraryFromDatabase(String itineraryNumber)
            throws InvalidTransactionException {
        if (!userType.equals("admin")) {
            String message = "Clients are not allowed to ";
            message += "delete Itinerary in database @ ";
            message += "deleteItineraryFromDatabase";
            throw new InvalidTransactionException(message);
        }
        if (!containsItinerary(itineraryNumber)) {
            String message = "No such Itinerary ";
            message += "@ deleteItineraryFromDatabase";
            throw new InvalidTransactionException(message);
        }
        itineraryDatabase.remove(itineraryNumber);
    }

    /**
     * Returns true if the flight database contains the flight s.t
     * flight # == flightNum
     * @param flightNum the flight # of the wanted flight
     * @return true if the flight database contains the wanted flight
     */
    public boolean containsFlight(String flightNum) {
        return flightDatabase.containsKey(flightNum);
    }

    /**
     * Returns the Flight whose flight number is flightnumber.
     * @param flightNumber the flight number of the wanted Flight
     * @return the Flight whose flight number is flightnumber
     * @throws InvalidTransactionException if the flight database
     * doesn't have a Flight whose flight number is flightnumber
     */
    public Flight getFlight(String flightNumber)
            throws InvalidTransactionException {
        if (!containsFlight(flightNumber)) {
            String msg = "No such Flight @ getFlight";
            throw new InvalidTransactionException(msg);
        }
        return flightDatabase.get(flightNumber);
    }

    /**
     * Returns the list of flights in this flight booking app.
     * @return the list of flights in this flight booking app
     */
    public HashMap<String, Flight> getFlightDatabase() {
        return flightDatabase;
    }

    /**
     * Adds the given Flight to Flight database.
     * @param f the Flight to be added onto database
     * @throws InvalidTransactionException if the Client tried to add
     * the Flight, or if the database already has the same Flight
     */
    public void addFlightToDatabase(Flight f)
            throws InvalidTransactionException {
        if (containsFlight(f.getFlightNumber())) {
            String message = "Flight number is already taken ";
            message += "@ addFlightToDatabase";
            throw new InvalidTransactionException(message);
        }
        flightDatabase.put(f.getFlightNumber(), f);
    }

    /**
     * Deletes the Flight from the Flight database.
     * @param f the Flight to be added onto the database.
     * @throws InvalidTransactionException
     *             if we couldn't find the Flight we want to delete inside
     *             the database
     */
    public void deleteFlightFromDatabase(String flightNumber)
            throws InvalidTransactionException {
        if (!userType.equals("admin")) {
            String message = "Clients are not allowed to ";
            message += "delete Flight in database @ ";
            message += "deleteFlightFromDatabase";
            throw new InvalidTransactionException(message);
        }
        if (!containsFlight(flightNumber)) {
            String message = "No such Flight @ deleteFlightFromDatabase";
            throw new InvalidTransactionException(message);
        }
        flightDatabase.remove(flightNumber);
    }

    public boolean containsClient(String email) {
        return clientDatabase.containsKey(email);
    }

    /**
     * Returns the Client whose email address is email.
     * @param email the email address of the wanted Client
     * @return the Client whose email address is email
     * @throws InvalidTransactionException if the client database
     * doesn't have the Client whose email address is email
     */
    public Client getClient(String email)
            throws InvalidTransactionException {
        if (!containsClient(email)) {
            String msg = "No such Client @ getClient";
            throw new InvalidTransactionException(msg);
        }
        return clientDatabase.get(email);
    }

    /**
     * Returns the list of clients in this flight booking app.
     * @return the list of clients in this flight booking app
     */
    public HashMap<String, Client> getClientDatabase() {
        return clientDatabase;
    }

    /**
     * Adds the given Client to Client database.
     * @param c the Client to be added onto database
     * @throws InvalidTransactionException
     *             if the database already has the same Client
     */
    public void addClientToDatabase(Client c)
            throws InvalidTransactionException {
        if (containsClient(c.getEmail())) {
            String message = "Email is already taken ";
            message += "@ addClientToDatabase";
            throw new InvalidTransactionException(message);
        }
        clientDatabase.put(c.getEmail(), c);
    }

    /**
     * Deletes the given Client from the Client database.
     * @param c the Client to be deleted from the database
     * @throws InvalidTransactionException if the database doesn't
     * have the Client we want to delete
     */
    public void deleteClientFromDatabase(String email)
            throws InvalidTransactionException {
        if (!containsClient(email)) {
            String message = "No such Client @ deleteClientFromDatabase";
            throw new InvalidTransactionException(message);
        }
        clientDatabase.remove(email);
    }

}
