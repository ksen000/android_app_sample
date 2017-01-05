package data;

import java.util.*;

import managers.MainSystem;

/**
 * A class representing a Searcher of Flight.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class SearchFlights<T> extends Search<T> {

    /** The departure date of the wanted Flight. */
    private String date;

    /** The origin of the wanted Flight. */
    private String origin;

    /** The destination of the wanted Flight. */
    private String destination;

    /**
     * Creates a new SearchFlights object with the given system,
     * departure date, origin, and the destination of the wanted Flight.
     * @param system the system of this flight booking application
     * @param date the departure date of the wanted Flight
     * @param origin the origin of the wanted Flight
     * @param destination the destination of the wanted Flight
     */
    public SearchFlights(MainSystem system, String date, String origin,
            String destination) {
        super(system);
        this.date = date;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Searches the wanted Flight. Returns the list of wanted Flight.
     * @return the list of wanted Flight
     */
    public void search() {
        HashMap<String, Flight> flightDatabase = system.getFlightDatabase();
        List<Flight> result = new ArrayList<Flight>();

        for (String flightNumber : flightDatabase.keySet()) {
            Flight f = flightDatabase.get(flightNumber);
            String targetDate = f.getDepartureDateTime().split(" ")[0];
            if (!date.equals("any"))
                if (!targetDate.equals(date))
                    continue;
            if (!origin.equals("any"))
                if (!f.getOrigin().equals(origin))
                    continue;
            if (!destination.equals("any"))
                if (!f.getDestination().equals(destination))
                    continue;
            result.add(f);
        }

        searchResult = (List<T>) result;
        return;
    }

    /**
     * Returns the list of flights gained by searching. Unlike
     * getSearchResult(), it returns Flight object instead of String
     * representation.
     * @return the list of flights gained by searching
     */
    public ArrayList<Flight> getSearchedFlight() {
        return (ArrayList<Flight>) searchResult;
    }

    /**
     * Sets the list of flights into search result.
     * There is some activity that passes search objects by intent.
     * Hence we need to put the list of flights into this search object.
     * @param flights the list of flights we want to pass between
     * activities in the android activities
     */
    public void setSearchResult(ArrayList<Flight> flights) {
        searchResult = (List<T>) flights;
    }

    /**
     * Returns true if the search criteria hasn't changed.
     * @param date the departure date to be compared
     * @param origin the origin to be compared
     * @param destination the destination to be compared
     * @return true if the search criteria hasn't changed
     */
    public boolean isSameSearchCriteria(String date, String origin,
            String destination) {
        if (!this.date.equals(date))
            return false;
        if (!this.origin.equals(origin))
            return false;
        if (!this.destination.equals(destination))
            return false;
        return true;
    }
}
