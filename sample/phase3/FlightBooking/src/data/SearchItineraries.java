package data;

import java.util.*;
import java.util.concurrent.TimeUnit;

import managers.MainSystem;

/**
 * A class representing a Searcher of Itinerary.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class SearchItineraries<T> extends Search<T> {

    /** The departure date of the wanted Itinerary. */
    private String date;

    /** The origin of the wanted Itinerary. */
    private String origin;

    /** The destination of the wanted Itinerary. */
    private String destination;

    /** Itinerary database imported from the MainSystem */
    private HashMap<String, Itinerary> itineraryDatabase;

    /** The temporary storage of Itinerary. */
    private List<Itinerary> forSearching;

    /**
     * Creates a new SearchItineraries object with the given system,
     * departure date, origin, and the destination of the wanted Itinerary.
     * @param system the main system of this flight booking application
     * @param date the departure date of the wanted Itinerary
     * @param origin the origin of the wanted Itinerary
     * @param destination the destination of the wanted Itinerary
     */
    public SearchItineraries(MainSystem system, String date, String origin,
            String destination) {
        super(system);
        this.date = date;
        this.origin = origin;
        this.destination = destination;
    }

    /**
     * Returns the list of itineraries gained by searching.
     * Unlike getSearchResult(), it returns Itinerary object
     * instead of String representation.
     * @return the list of itineraries gained by searching
     */
    public ArrayList<Itinerary> getSearchedItinerary() {
        return (ArrayList<Itinerary>) searchResult;
    }

    /**
     * Sets the list of itineraries into search result.
     * There is some activity that passes search objects by intent.
     * Hence we need to put the list of itineraries into this
     * search object.
     * @param e the list of itineraries we want to pass between
     * activities in the android activities
     */
    public void setSearchResult(ArrayList<Itinerary> e) {
        searchResult = (List<T>) e;
    }

    /**
     * Sorts the search result by the cost of Itinerary.
     */
    public void sortSearchResultByCost() {
        Collections.sort(searchResult, new SortByCost());
    }

    /**
     * Sorts the search result by the travel time of Itinerary.
     */
    public void sortSearchResultByTime() {
        Collections.sort(searchResult, new SortByTime());
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

    /**
     * Searches, creates the list of wanted Itinerary.
     * It will set the search result onto searchResult
     * @throws InvalidTransactionException
     */
    public void search() throws InvalidTransactionException {

        itineraryDatabase = system.getItineraryDatabase();

        List<Itinerary> result = new ArrayList<Itinerary>();

        for (String itineraryNumber : itineraryDatabase.keySet()) {
            Itinerary e = itineraryDatabase.get(itineraryNumber);
            String otherDate = e.getDepartureDateTime().split(" ")[0];
            if (!otherDate.equals(date))
                continue;
            if (!e.getOrigin().equals(origin))
                continue;
            if (!e.getDestination().equals(destination))
                continue;
            result.add(e);
        }

        if (result.size() != 0) {
            searchResult = (List<T>) result;
            return;
        }

        // We couldn't find desired Itinerary in database.
        // Maybe we can create one from the flight database.
        boolean itineraryFound = createItineraryFromFlights();

        // We've created Itinerary, and we found one!
        if (itineraryFound) {
            search();
            return;
        }

        // No desired Itinerary. So returns nothing.
        searchResult = (List<T>) result;
    }

    /**
     * Searches the flight database.
     * Creates new Itinerary.
     * Adds the newly created Itinerary to Itinerary database.
     * Returns true if the wanted Itinerary is created.
     * @return true if the wanted Itinerary is created.
     * @throws InvalidTransactionException
     */
    private boolean createItineraryFromFlights()
            throws InvalidTransactionException {

        TreeSet<String> visited = new TreeSet<String>();
        visited.add(origin);
        forSearching = new ArrayList<Itinerary>();
        searchConnection(origin, destination, visited,
                new ArrayList<Flight>());

        boolean itineraryFound = false;
        for (Itinerary e : forSearching) {
            String otherDate = e.getDepartureDateTime().split(" ")[0];
            if (date.equals(otherDate) && origin.equals(e.getOrigin()))
                if (destination.equals(e.getDestination()))
                    itineraryFound = true;
            system.addItineraryToDatabase(e);
        }

        return itineraryFound;
    }

    /**
     * Searches the connection between Flights.
     * Creates a new Itinerary.
     * Adds the newly create Itinerary to Itinerary database.
     * @param origin the origin of wanted Itinerary
     * @param destination the destination of wanted Itinerary
     * @param visited the list of places you have visited
     * @param history the list of Flights you took
     * @throws InvalidTransactionException
     */
    private void searchConnection(String origin, String destination,
            TreeSet<String> visited, List<Flight> history)
            throws InvalidTransactionException {

        SearchFlights searchF = new SearchFlights(system,
                "any", origin, "any");
        searchF.search();
        List<Flight> flights = searchF.getSearchedFlight();

        for (Flight f : flights) {
            
            // No space available.
            if (f.getSpace() <= 0) {
                continue;
            }
            
            // We don't want to go back.
            if (visited.contains(f.getDestination())) {
                continue;
            }

            if (history.size() != 0) {
                Flight latestFlight = history.get(history.size() - 1);

                // If we arrive later than the departure time of
                // next flight, then we can't take this flight.
                if (f.compareTo(latestFlight) <= 0) {
                    continue;
                }

                // If lay-over time is more than 6 hours,
                // we don't want to wait that long.
                double sixHours = TimeUnit.HOURS.toMillis(6);
                double arrivedTime = latestFlight.getNumericalArrivalTime();
                double deptTime = f.getNumericalDepartureTime();
                if (deptTime - arrivedTime > sixHours) {
                    continue;
                }
            }

            if (f.getDestination().equals(destination)) {
                int num = itineraryDatabase.size() + forSearching.size() + 1;
                String itineraryNumber = Integer.toString(num);
                Itinerary e = new Itinerary(itineraryNumber);
                e.addFlight(f);
                for (Flight ff : history)
                    e.addFlight(ff);
                forSearching.add(e);
                continue;
            }
            history.add(f);
            visited.add(f.getDestination());
            searchConnection(
                    f.getDestination(), destination, visited, history);
            history.remove(f);
            visited.remove(f.getDestination());
        }
    }

    /*
     * Sorts the search result by travel time of Itinerary.
     */
    private class SortByTime implements Comparator<T> {
        public int compare(T a, T b) {
            Itinerary x = (Itinerary) a;
            Itinerary y = (Itinerary) b;
            if (x.getTotalTravelTime() < y.getTotalTravelTime())
                return -1;
            if (x.getTotalTravelTime() > y.getTotalTravelTime())
                return 1;
            return 0;
        }
    }

    /*
     * Sorts the search result by the cost of Itinerary.
     */
    private class SortByCost implements Comparator<T> {
        public int compare(T a, T b) {
            Itinerary x = (Itinerary) a;
            Itinerary y = (Itinerary) b;
            int res = 0;
            if (x.getTotalCost() < y.getTotalCost())
                res = -1;
            if (x.getTotalCost() > y.getTotalCost())
                res = 1;
            return res;
        }
    }

}
