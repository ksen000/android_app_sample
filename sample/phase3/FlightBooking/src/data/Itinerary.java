package data;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.text.*;

/**
 * A class representing an Itinerary.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class Itinerary {

    /** The list of flights required to complete this Itinerary. */
    private TreeSet<Flight> flights;

    /** The flight numbers of flights in this Itinerary. */
    private TreeSet<String> flightNumbers;

    /** The itinerary number of this Itinerary. */
    private String itineraryNumber;

    /** The capacity of this Itinerary. i.e. min(flight[all i].capacity). */
    private int capacity;

    /** The available space of this Itinerary. */
    private int reservationSpace;

    /** The departure date and time of this Itinerary. */
    private String departureDateTime;

    /** The arrival date and time of this Itinerary. */
    private String arrivalDateTime;

    /** The origin of this Itinerary. */
    private String origin;

    /** The destination of this Itinerary. */
    private String destination;

    /** The total cost of this Itinerary. */
    private double totalCost;

    /**
     * Creates a new Itinerary with the given itinerary number.
     * @param itineraryNumber the itinerary number of this Itinerary.
     */
    public Itinerary(String itineraryNumber) {
        this.itineraryNumber = itineraryNumber;

        // sorted by the arrival time.
        this.flights = new TreeSet<Flight>(new MyComparator());
        this.flightNumbers = new TreeSet<String>();
    }

    /*
     * It automatically sorts the list of flight by its arrival time.
     */
    private class MyComparator implements Comparator<Flight> {
        public int compare(Flight a, Flight b) {
            double aTime = a.getNumericalArrivalTime();
            double bTime = b.getNumericalArrivalTime();
            if (aTime < bTime)
                return -1;
            if (aTime > bTime)
                return 1;
            return 0;
        }
    }

    /**
     * Sets all the information of this Itinerary.
     * The information of this Itinerary depends on the flights
     * inside this Itinerary.
     */
    public void setAllItineraryInfo() {
        this.capacity = 987654321;
        this.reservationSpace = 987654321;
        this.totalCost = 0.0;
        for (Flight f : flights) {
            this.capacity = Math.min(this.capacity, f.getCapacity());
            this.totalCost += f.getCost();
            this.reservationSpace = Math.min(this.reservationSpace,
                    f.getSpace());
        }
        if(flights.size() == 0)
            return;
        Flight firstFlight = flights.first();
        Flight lastFlight = flights.last();
        this.departureDateTime = firstFlight.getDepartureDateTime();
        this.arrivalDateTime = lastFlight.getArrivalDateTime();
        this.origin = firstFlight.getOrigin();
        this.destination = lastFlight.getDestination();
    }

    /**
     * Adds the Flight to this Itinerary.
     * @param f the Flight to be added to this Itinerary
     * @throws InvalidTransactionException
     *             if this Itinerary already has the same Flight
     */
    public void addFlight(Flight f)
            throws InvalidTransactionException {
        if (flightNumbers.contains(f.getFlightNumber())) {
            String message = "Can't add the same Flight again @ addFlight";
            throw new InvalidTransactionException(message);
        }
        flights.add(f);
        flightNumbers.add(f.getFlightNumber());
        setAllItineraryInfo();
    }

    /**
     * Removes the FLight from this Itinerary.
     * @param f the Flight to be removed from this Itinerary
     * @throws InvalidTransactionException if this Itinerary doesn't
     * have the Flight we want to delete
     */
    public void removeFlight(Flight f)
            throws InvalidTransactionException {
        if (!flightNumbers.contains(f.getFlightNumber())) {
            String message = "No such Flight @ removeFlight";
            throw new InvalidTransactionException(message);
        }
        flights.remove(f);
        flightNumbers.remove(f.getFlightNumber());
        setAllItineraryInfo();
    }

    /**
     * Returns the list of flights required to complete this Itinerary.
     * @return the list of flights required to complete this Itinerary
     */
    public TreeSet<Flight> getFlights() {
        return flights;
    }

    /**
     * Returns the itinerary number of this Itinerary.
     * @return the itinerary number of this Itinerary
     */
    public String getItineraryNumber() {
        return itineraryNumber;
    }

    /**
     * Returns the capacity of this Itinerary.
     * @return the capacity of this Itinerary
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Returns the number of available space of this Itinerary.
     * @return the number of available space of this Itinerary
     */
    public int getReservationSpace() {
        return reservationSpace;
    }

    /**
     * Returns the departure date and time of this Itinerary.
     * @return the departure date and time of this Itinerary
     */
    public String getDepartureDateTime() {
        return departureDateTime;
    }

    /**
     * Returns the arrival date and time of this Itinerary.
     * @return the arrival date and time of this Itinerary
     */
    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    /**
     * Returns the origin of this Itinerary.
     * @return the origin of this Itinerary
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Returns the destination of this Itinerary.
     * @return the destination of this Itinerary
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the price of this Itinerary.
     * @return the price of this Itinerary
     */
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Returns the total travel time of this Itinerary.
     * @return the total travel time of this Itinerary
     */
    public double getTotalTravelTime() {
        Flight firstFlight = flights.first();
        Flight lastFlight = flights.last();
        double arriveTime = lastFlight.getNumericalArrivalTime();
        double deptTime = firstFlight.getNumericalDepartureTime();
        return arriveTime - deptTime;
    }

    /**
     * A String representation of this Itinerary.
     */
    public String toString() {
        String res = "";
        DecimalFormat df = new DecimalFormat("#.00");
        for (Flight f : getFlights()) {
            res += f.getFlightNumber() + ",";
            res += f.getDepartureDateTime() + ",";
            res += f.getArrivalDateTime() + ",";
            res += f.getAirline() + ",";
            res += f.getOrigin() + ",";
            res += f.getDestination() + "\n";
        }
        res += "$" + df.format(getTotalCost()) + "\n";
        long millis = (long) getTotalTravelTime();
        String hm = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis)
                        - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millis)));
        res += hm + "\n";

        return res;
    }
}
