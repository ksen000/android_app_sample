package data;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A class representing a Client.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class Client {

    /** The shopping cart. */
    private TreeSet<Itinerary> shoppingCart;

    /** Itinerary numbers in shopping cart. */
    private TreeSet<String> iNumbersInShoppingCart;

    /** Booked itineraries. */
    private TreeSet<Itinerary> bookedItineraries;

    /** Itinerary numbers in booked itineraries. */
    private TreeSet<String> iNumbersInBooked;

    /** Email address of this Client. */
    private String email;

    /** Password of this Client. */
    private String password;

    /** The first name of this Client. */
    private String firstname;

    /** The last name of this Client. */
    private String lastname;

    /** The credit card number of this Client. */
    private String creditCardNumber;

    /** The expiry date of this Client's credit card. */
    private String expiryDate;

    /** The address of this Client */
    private String address;

    /**
     * Creates a new Client with the given email address.
     * @param email the email address of this client
     */
    public Client(String email) {
        this.email = email;
        this.shoppingCart = new TreeSet<Itinerary>(
                new SortByItineraryNumber());
        this.bookedItineraries = new TreeSet<Itinerary>(
                new SortByItineraryNumber());

        // It is used to avoid adding the same Itinerary into
        // shopping cart.
        this.iNumbersInShoppingCart = new TreeSet<String>();

        // It is used to avoid adding the same Itinerary
        // into booked Itinerary list.
        this.iNumbersInBooked = new TreeSet<String>();
    }

    /**
     * Creates a new Client with the given last name,
     * first name, email address, address, credit card number,
     * and its expiry date.
     * @param lastname the last name of this Client
     * @param firstname the first name of this Client
     * @param email the email address of this Client
     * @param address the address of this Client
     * @param creditCardNumber the credit card number of this Client
     * @param expiryDate the expiry date of this Client's credit card
     * @throws InvalidTransactionException if the given expiry date is
     * not following the format (YYYY-MM-DD).
     */
    public Client(String lastname, String firstname, String email,
            String address, String creditCardNumber, String expiryDate)
            throws InvalidTransactionException {
        if (isInvalidDate(expiryDate)) {
            String message = "ExpiryDate format is invalid ";
            message += "@ Client constructor.";
            throw new InvalidTransactionException(message);
        }
        this.shoppingCart = new TreeSet<Itinerary>(
                new SortByItineraryNumber());
        this.iNumbersInShoppingCart = new TreeSet<String>();
        this.bookedItineraries = new TreeSet<Itinerary>(
                new SortByItineraryNumber());
        this.iNumbersInBooked = new TreeSet<String>();
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.expiryDate = expiryDate;
    }

    /**
     * Returns true if the expiry date of this Client's credit card is not
     * following the format (YYYY-MM-DD).
     * @param date the expiry date of this Cilent's credit card
     * @return true if expiry date date is not following
     * the format (YYYY-MM-DD)
     */
    private boolean isInvalidDate(String date) {

        // the length of YYYY-MM-DD is 10.
        if (date.length() != 10)
            return true;

        Date formattedDate = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH);
        try {
            formattedDate = formatter.parse(date);
            formattedDate.getTime();
        } catch (Exception e) {
            return true;
        }

        int month = Integer.parseInt(date.substring(5, 7));
        int date2 = Integer.parseInt(date.substring(8, 10));
        if (!(1 <= month && month <= 12))
            return true;
        if (!(1 <= date2 && date2 <= 31))
            return true;

        return false;
    }

    /*
     * It automatically sorts the list of Itinerary by the itinerary number.
     */
    private class SortByItineraryNumber implements Comparator<Itinerary> {
        public int compare(Itinerary a, Itinerary b) {
            return a.getItineraryNumber().compareTo(b.getItineraryNumber());
        }
    }

    /**
     * Adds the given Itinerary into this Client's shopping cart.
     * @param e the Itinerary which this Client wants to add
     * to his shopping cart
     * @throws InvalidTransactionException if this Client already have
     * the same Itinerary inside his shopping cart
     */
    public void addItineraryToShoppingCart(Itinerary e)
            throws InvalidTransactionException {
        if (iNumbersInShoppingCart.contains(e.getItineraryNumber())) {
            String message = "Can't add the same Itinerary again ";
            message += "@ addItineraryToShoppingCart";
            throw new InvalidTransactionException(message);
        }
        if (iNumbersInBooked.contains(e.getItineraryNumber())) {
            String message = "You've already booked this Itinerary ";
            message += "@ addItineraryToShoppingCart";
            throw new InvalidTransactionException(message);
        }
        shoppingCart.add(e);
        iNumbersInShoppingCart.add(e.getItineraryNumber());
    }

    /**
     * Deletes the given Itinerary from this Client's shopping cart.
     * @param e the Itinerary which this Client wants to
     * delete from his shopping cart
     * @throws InvalidTransactionException if the shopping cart doesn't
     * have the Itinerary we want to delete
     */
    public void deleteItineraryFromShoppingCart(Itinerary e)
            throws InvalidTransactionException {
        if (!iNumbersInShoppingCart.contains(e.getItineraryNumber())) {
            String message = "You don't even have this Itinerary ";
            message += "@ deleteItineraryInShoppingCart";
            throw new InvalidTransactionException(message);
        }
        shoppingCart.remove(e);
        iNumbersInShoppingCart.remove(e.getItineraryNumber());
    }

    /**
     * Clears all the Itineraries in the shopping cart.
     */
    public void clearAllItinerariesInShoppingCart() {
        shoppingCart = new TreeSet<Itinerary>();
        iNumbersInShoppingCart = new TreeSet<String>();
    }

    /**
     * Books an Itinerary.
     * @param e the Itinerary which this Client wants to book
     * @throws InvalidTransactionException if this Client has already
     * booked the same Itinerary
     */
    public void bookItinerary(Itinerary e)
            throws InvalidTransactionException {
        if (iNumbersInBooked.contains(e.getItineraryNumber())) {
            String message = "Can't add the same Itinerary again ";
            message += "@ bookItinerary";
            throw new InvalidTransactionException(message);
        }
        if (iNumbersInShoppingCart.contains(e.getItineraryNumber())) {

            // You don't need this Itinerary in shopping cart anymore.
            deleteItineraryFromShoppingCart(e);
        }
        bookedItineraries.add(e);
        iNumbersInBooked.add(e.getItineraryNumber());
        for (Flight flight : e.getFlights()) {
            flight.addClient(this);
        }
    }

    /**
     * Cancels a booked Itinerary.
     * Removes this Client from all the Flight he was planning to take.
     * @param e the Itinerary to be cancelled
     * @throws InvalidTransactionException if the booked Itinerary
     * list doesn't have the Itinerary we want to delete
     */
    public void cancelBookedItinerary(Itinerary e)
            throws InvalidTransactionException {
        if (!iNumbersInBooked.contains(e.getItineraryNumber())) {
            String message = "You don't even have this Itinerary ";
            message += "@ cancelBookedItinerary";
            throw new InvalidTransactionException(message);
        }
        bookedItineraries.remove(e);
        iNumbersInBooked.remove(e.getItineraryNumber());
        for (Flight flight : e.getFlights()) {
            flight.removeClient(this);
        }
    }

    /**
     * Cancels all the booked Itineraries.
     * @throws InvalidTransactionException if the booked Itinerary
     * list doesn't have the Itinerary we want to delete
     */
    public void cancelAllBookedItineraries()
            throws InvalidTransactionException {
        for (Itinerary e : bookedItineraries) {
            cancelBookedItinerary(e);
        }
    }

    /**
     * Returns all the booked Itineraries.
     * @return all the booked Itineraries
     */
    public TreeSet<Itinerary> getBookedItineraries() {
        return this.bookedItineraries;
    }

    /**
     * Returns the shopping cart of this Client.
     * @return the shopping card of this Client
     */
    public TreeSet<Itinerary> getShoppingCart() {
        return this.shoppingCart;
    }

    /**
     * Returns the email address of this Client.
     * @return the email address of this Client
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of this Client to email.
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of this Client.
     * @return the password of this Client
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of this Client to password.
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the first name of this Client.
     * @return the first name of this Client
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * Sets the first name of this Client to firstname.
     * @param firstname the new first name
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * Returns the last name of this Client.
     * @return the last name of this Client
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * Sets the last name of this Client to lastname.
     * @param lastname the new last name
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * Returns the credit card number of this Client.
     * @return the credit card number of this Client
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * Sets the credit card number of this Client to creditCardNumber.
     * @param creditCardNumber the new credit card number
     */
    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * Returns the expiry date of this Client's credit card.
     * @return the expiry date of this Client's credit card
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Sets the expiry date of this Client's credit card to expiryDate.
     * @param expiryDate the new expiryDate
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Returns the address of this Client.
     * @return the address of this Client
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of this Client to address.
     * @param address the new address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * A String representation of this Client.
     */
    public String toString() {
        String res = lastname + ",";
        res += firstname + ",";
        res += email + ",";
        res += address + ",";
        res += creditCardNumber + ",";
        res += expiryDate;
        return res;
    }

    /**
     * Another String representation of this Client.
     * Used when we have to save permanent data
     * @return String representation of this Client
     */
    public String getStringRepr() {
        String res = email + ",";
        res += password + ",";
        res += firstname + ",";
        res += lastname + ",";
        res += creditCardNumber + ",";
        res += expiryDate + ",";
        res += address;
        return res;
    }
}
