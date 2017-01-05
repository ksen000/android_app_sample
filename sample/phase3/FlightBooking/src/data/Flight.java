package data;

import java.util.*;
import java.text.*;

/**
 * A class representing a Flight.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class Flight implements Comparable<Flight> {

    /** List of clients planning to take this Flight. */
    private TreeSet<Client> passengers;

    /** List of email addresses of clients planning to take this Flight. */
    private TreeSet<String> clientEmails;

    /** The flight number of this Flight. */
    private String flightNumber;

    /** The departure date and time of this Flight. (YYYY-MM-DD hh:mm) */
    private String departureDateTime;

    /** The arrival date and time of this Flight. (YYYY-MM-DD hh:mm) */
    private String arrivalDateTime;

    /** The airline of this Flight */
    private String airline;

    /** The origin of this Flight */
    private String origin;

    /** The destination of this Flight. */
    private String destination;

    /** The price of this Flight. */
    private double cost;

    /** The maximum number of seat. */
    private int capacity;

    /**
     * Creates a new Flight with the given flight number.
     * @param flightNumber the flight number of this Flight
     */
    public Flight(String flightNumber) {
        this.passengers = new TreeSet<Client>(new SortByEmail());
        this.clientEmails = new TreeSet<String>();
        this.flightNumber = flightNumber;
        this.capacity = 300;
    }

    /**
     * Creates a new Flight with the given flight number,
     * departure date and time, arrival date and time, airline,
     * origin, destination, cost.
     * @param flightNumber the flight number of this Flight
     * @param departureDateTime the departure date and time of this Flight
     * @param arrivalDateTime the arrival date and time of this Flight
     * @param airline the airline of this Flight
     * @param origin the origin of this Flight
     * @param destination the destination of this Flight
     * @param cost the price of this Flight
     * @throws InvalidTransactionException if the given departure/arrival
     * date and time is not following the format (YYYY-MM-DD hh:mm),
     * or if the given cost following the format (should be double).
     */
    public Flight(String flightNumber, String departureDateTime,
            String arrivalDateTime, String airline, String origin,
            String destination, String cost, String capacity)
            throws InvalidTransactionException {
        if (isInvalidDateTime(departureDateTime, arrivalDateTime)) {
            String message = "DateTime format is invalid ";
            message += "@ Flight constructor.";
            throw new InvalidTransactionException(message);
        }
        if (origin.equals(destination)) {
            String message = "Origin - Destination is invalid ";
            message += "@ Flight constructor.";
            throw new InvalidTransactionException(message);
        }
        try {
            Double.parseDouble(cost);
            Integer.parseInt(capacity);
        } catch (Exception e) {
            String message = "The given cost is invalid ";
            message += "@ Flight constructor.";
            throw new InvalidTransactionException(message);
        }
        try {
            Integer.parseInt(capacity);
        } catch (Exception e) {
            String message = "The given capacity is invalid ";
            message += "@ Flight constructor.";
            throw new InvalidTransactionException(message);
        }
        this.passengers = new TreeSet<Client>(new SortByEmail());
        this.clientEmails = new TreeSet<String>();
        this.flightNumber = flightNumber;
        this.departureDateTime = departureDateTime;
        this.arrivalDateTime = arrivalDateTime;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.cost = Double.parseDouble(cost);
        this.capacity = Integer.parseInt(capacity);
    }

    /**
     * Returns true if the departure/arrival date and time of this
     * Flight is not following the format.
     * @param departureDateTime the departure date and time of this Flight
     * @param arrivalDateTime the arrival date and time of this Flight
     * @return true if the given date and time is not following the format
     */
    private boolean isInvalidDateTime(String departureDateTime,
            String arrivalDateTime) {

        // the length of YYYY-MM-DD HH:MM is 10.
        if (departureDateTime.length() != 16)
            return true;
        if (arrivalDateTime.length() != 16)
            return true;

        Date formattedDeptDate = getFormattedDate(departureDateTime);
        Date formattedArrivalDate = getFormattedDate(arrivalDateTime);
        if (formattedDeptDate == null || formattedArrivalDate == null) {
            return true;
        }
        double deptTime = 0.0;
        double arrivalTime = 0.0;
        try {
            deptTime = formattedDeptDate.getTime();
            arrivalTime = formattedArrivalDate.getTime();
        } catch (Exception e) {
            return true;
        }

        int monthDept = Integer.parseInt(departureDateTime.substring(5, 7));
        int dateDept = Integer.parseInt(departureDateTime.substring(8, 10));
        int hourDept = Integer.parseInt(departureDateTime.substring(11, 13));
        int minDept = Integer.parseInt(departureDateTime.substring(14, 16));
        int monthArrive = Integer.parseInt(arrivalDateTime.substring(5, 7));
        int dateArrive = Integer.parseInt(arrivalDateTime.substring(8, 10));
        int hourArrive = Integer.parseInt(arrivalDateTime.substring(11, 13));
        int minArrive = Integer.parseInt(arrivalDateTime.substring(14, 16));

        if (!(1 <= monthDept && monthDept <= 12))
            return true;
        if (!(1 <= dateDept && dateDept <= 31))
            return true;
        if (!(0 <= hourDept && hourDept <= 23))
            return true;
        if (!(0 <= minDept && minDept <= 59))
            return true;
        if (!(1 <= monthArrive && monthArrive <= 12))
            return true;
        if (!(1 <= dateArrive && dateArrive <= 31))
            return true;
        if (!(0 <= hourArrive && hourArrive <= 23))
            return true;
        if (!(0 <= minArrive && minArrive <= 59))
            return true;

        return deptTime >= arrivalTime;
    }

    /*
     * It automatically sorts the list of Client by the email address.
     */
    private class SortByEmail implements Comparator<Client> {
        public int compare(Client a, Client b) {
            return a.getEmail().compareTo(b.getEmail());
        }
    }

    /**
     * Adds the Client to this Flight.
     * @param c the Client to be added into this Flight
     * @throws InvalidTransactionException if there is no space available
     * or the given client has already booked this Flight
     */
    public void addClient(Client c) throws InvalidTransactionException {
        if (passengers.size() >= capacity) {
            String message = "The seat for Flight is full @ addClient";
            throw new InvalidTransactionException(message);
        }
        if (clientEmails.contains(c.getEmail())) {
            String message = "You already booked this Flight @ addClient";
            throw new InvalidTransactionException(message);
        }
        passengers.add(c);
        clientEmails.add(c.getEmail());
    }

    /**
     * Removes the Client from this Flight.
     * @param c the Client to be removed from this Flight
     * @throws InvalidTransactionException
     *             if the given Client hasn't booked this Flight.
     */
    public void removeClient(Client c) throws InvalidTransactionException {
        if (!clientEmails.contains(c.getEmail())) {
            String message = "No such Client @ removeClient";
            throw new InvalidTransactionException(message);
        }
        passengers.remove(c);
        clientEmails.remove(c.getEmail());
    }

    /**
     * Returns the list of clients planning to take this Flight.
     * @return the list of clients planning to take this Flight
     */
    public TreeSet<Client> getPassengers() {
        return passengers;
    }

    /**
     * Returns the number of available spaces of this Flight.
     * @return the number of available spaces of this Flight
     */
    public int getSpace() {
        return capacity - passengers.size();
    }

    /**
     * Returns the number of seat in this Flight.
     * @return the number of seat in this Flight
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Sets the capacity of this Flight to capacity
     * @param capacity the number of seat in this Flight
     */
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Returns the flight number of this Flight.
     * @return the flight number of this Flight
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Sets the flight number of this Flight to flightnumber.
     * @param flightNumber the new flight number of this Flight
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Returns the departure date and time of this Flight.
     * @return the departure date and time of this Flight
     */
    public String getDepartureDateTime() {
        return this.departureDateTime;
    }

    /**
     * Returns the millisecond of the departure date and time of this Flight.
     * @return the millisecond of the departure date and time of this Flight
     */
    public double getNumericalDepartureTime() {
        Date deptDateAndTime = getFormattedDate(departureDateTime);
        return deptDateAndTime.getTime();
    }

    /**
     * Sets the departure date and time of this Flight to departureDateTime.
     * @param departureDateTime the new departure date and time
     * of this Flight
     */
    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    /**
     * Returns the arrival date and time of this Flight.
     * @return the arrival date and time of this Flight
     */
    public String getArrivalDateTime() {
        return this.arrivalDateTime;
    }

    /**
     * Returns the millisecond of the arrival date and time of this Flight.
     * @return the millisecond of the arrival date and time of this Flight
     */
    public double getNumericalArrivalTime() {
        Date arrivalDateAndTime = getFormattedDate(arrivalDateTime);
        return arrivalDateAndTime.getTime();
    }

    /**
     * Sets the arrival date and time of this Flight to arrivalDateTime.
     * @param arrivalDateTime the new arrival date and time of this
     */
    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    /**
     * Returns the airline of this Flight.
     * @return the airline of this Flight
     */
    public String getAirline() {
        return airline;
    }

    /**
     * Sets the airline of this Flight to airline.
     * @param airline the new airline of this Flight
     */
    public void setAirline(String airline) {
        this.airline = airline;
    }

    /**
     * Returns the origin of this Flight.
     * @return the origin of this Flight
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the origin of this Flight to origin.
     * @param origin the new origin of this Flight
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Returns the destination of this Flight.
     * @return the destination of this Flight
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the destination of this Flight to destination.
     * @param destination the new destination of this Flight
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Returns the price of this Flight.
     * @return the price of this Flight
     */
    public double getCost() {
        return cost;
    }

    /**
     * Sets the price of this Flight to cost.
     * @param cost the new price of this Flight
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Returns the formatted date.
     * @param date the departure/arrival date of this Flight in String.
     * @return the formatted date
     */
    private Date getFormattedDate(String date) {
        Date result = null;
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd kk:mm", Locale.ENGLISH);
        try {
            result = formatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
        return result;
    }

    /**
     * Returns the travel time of this Flight.
     * @return the travel time of this Flight
     */
    public double getTravelTime() {
        return getNumericalArrivalTime() - getNumericalDepartureTime();
    }

    /**
     * Compares this Flight with other flight, with this
     * Flight's arrival time and other Flight's departure time.
     */
    public int compareTo(Flight other) {
        Date otherArrival = getFormattedDate(other.arrivalDateTime);
        Date thisDept = getFormattedDate(this.departureDateTime);
        return thisDept.compareTo(otherArrival);
    }

    /**
     * A String representation of this Flight.
     */
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        String res = flightNumber + ",";
        res += departureDateTime + ",";
        res += arrivalDateTime + ",";
        res += airline + ",";
        res += origin + ",";
        res += destination + ",";
        res += df.format(cost);
        return res;
    }

    /**
     * Another String representation of this Client.
     * Used when we have to save permanent data
     * @return String representation of this Client
     */
    public String getStringRepr() {
        DecimalFormat df = new DecimalFormat("#.00");
        String res = flightNumber + ",";
        res += departureDateTime + ",";
        res += arrivalDateTime + ",";
        res += airline + ",";
        res += origin + ",";
        res += destination + ",";
        res += df.format(cost) + ",";
        res += Integer.toString(capacity);
        return res;
    }

}
