package managers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import data.*;

public class Driver {
    private static MainSystem system;
    private static SearchItineraries searchI;
    private static SearchFlights searchF;

    public Driver() {
        this.system = new MainSystem("admin");
        this.searchI = new SearchItineraries(system, "", "", "");
        this.searchF = new SearchFlights(system, "", "", "");
    }

    public static void uploadClientInfo(String path) throws IOException {
        FileReader myfile = new FileReader(path);
        BufferedReader br = new BufferedReader(myfile);
        String line;
        while ((line = br.readLine()) != null && line != "") {
            String tmpline = line.replace("\n", "");
            String[] s = tmpline.split(",");
            try {
                Client newClient = new Client(
                        s[0], s[1], s[2], s[3], s[4], s[5]);
                system.addClientToDatabase(newClient);
            } catch (InvalidTransactionException e) {
                System.out.println(e.getMessage());
            }
        }
        br.close();
    }

    public static void uploadFlightInfo(String path) throws IOException {
        FileReader myfile = new FileReader(path);
        BufferedReader br = new BufferedReader(myfile);
        String line;
        while ((line = br.readLine()) != null && line != "") {
            String tmpline = line.replace("\n", "");
            String[] s = tmpline.split(",");
            try {
                Flight newFlight = new Flight(
                        s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7]);
                system.addFlightToDatabase(newFlight);
            } catch (InvalidTransactionException e) {
                System.out.println(e.getMessage());
            }
        }
        br.close();
    }

    public static String getClient(String email) {
        Client client;
        try {
            client = system.getClient(email);
        } catch (InvalidTransactionException e) {
            System.out.println(e.getMessage());
            return "";
        }
        return client.toString();
    }

    public static String getFlights(
            String date, String origin, String destination) {
        searchF = new SearchFlights(system, date, origin, destination);
        searchF.search();
        return searchF.getSearchResult();
    }

    public static String getItineraries(
            String date, String origin, String destination) {
        searchI = new SearchItineraries(system, date, origin, destination);
        try {
            searchI.search();
        } catch (InvalidTransactionException e) {
            System.out.println(e.getMessage());
        }
        return searchI.getSearchResult();
    }

    public static String getItinerariesSortedByCost(
            String date, String origin, String destination) {
        searchI = new SearchItineraries(system, date, origin, destination);
        try {
            searchI.search();
        } catch (InvalidTransactionException e) {
            System.out.println(e.getMessage());
        }
        searchI.sortSearchResultByCost();
        return searchI.getSearchResult();
    }

    public static String getItinerariesSortedByTime(
            String date, String origin, String destination) {
        searchI = new SearchItineraries(system, date, origin, destination);
        try {
            searchI.search();
        } catch (InvalidTransactionException e) {
            System.out.println(e.getMessage());
        }
        searchI.sortSearchResultByTime();
        return searchI.getSearchResult();
    }
}