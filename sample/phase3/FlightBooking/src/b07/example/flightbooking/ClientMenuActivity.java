package b07.example.flightbooking;

import java.util.ArrayList;
import java.util.TreeSet;

import data.Client;
import data.InvalidTransactionException;
import data.Itinerary;
import data.SearchItineraries;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * A class representing an Client menu of this flight booking application.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class ClientMenuActivity extends AdminMenuActivity {

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_menu);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    /** {@inheritDoc} */
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Goes to EditClientActivity.
     * Passes the email of the Client you want to edit by intent.
     * @param view
     */
    public void editClient(View view) {
        String email = system.getUserType();
        Intent intent = new Intent(this, EditClientActivity.class);
        intent.putExtra(MainActivity.EMAIL_KEY, email);
        startActivity(intent);
    }

    /**
     * Goes to SearchItineraryActivity.
     * Passes the email of the Client who wants to go search
     * and buy itineraries, by intent.
     * @param view
     */
    public void findItinerary(View view) {
        String email = system.getUserType();
        Intent intent = new Intent(this, SearchItineraryActivity.class);
        intent.putExtra(MainActivity.EMAIL_KEY, email);
        startActivity(intent);
    }

    /**
     * Goes to ChooseItineraryActivity.
     * Passes the email of the client who wants to see his list of
     * booked itineraries. Also passes the list of booked
     * itineraries.
     * @param view
     */
    public void viewBookedItinerary(View view) {
        Client c;
        String email = system.getUserType();
        try {
            c = system.getClient(email);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx);
            msg += ". Please try again.";
            Toast.makeText(
                    getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        TreeSet<Itinerary> booked = c.getBookedItineraries();
        ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
        for (Itinerary e : booked)
            itineraries.add(e);
        SearchItineraries searchI = new SearchItineraries(
                system, "", "", "");
        searchI.setSearchResult(itineraries);
        superVisor.setSearchObject(searchI);
        Intent intent = new Intent(this, ChooseItineraryActivity.class);
        intent.putExtra(MainActivity.EMAIL_KEY, email);
        startActivity(intent);
    }
    
    /**
     * Goes to ChooseItineraryActivity.
     * Passes the email of the client who wants to see his
     * shopping cart.
     * Passes the list of itineraries in shopping cart.
     * @param view
     */
    public void viewShoppingCart(View view) {
        Client c;
        String email = system.getUserType();
        try {
            c = system.getClient(email);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx);
            msg += ". Please try again.";
            Toast.makeText(
                    getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        TreeSet<Itinerary> cart = c.getShoppingCart();
        ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
        for (Itinerary e : cart)
            itineraries.add(e);
        SearchItineraries searchI = new SearchItineraries(
                system, "", "", "");
        searchI.setSearchResult(itineraries);
        superVisor.setSearchObject(searchI);
        Intent intent = new Intent(this, ChooseItineraryActivity.class);
        intent.putExtra(MainActivity.EMAIL_KEY, email);
        startActivity(intent);
    }

    /**
     * Logs out from this flight booking application. Goes to MainActivity.
     */
    public void logOut(View view) {
        // you have to implement the data upload program
        // inside this function.
        system.discardUserType();
        String msg = "Sucessfully logged out!";
        Toast.makeText(
                getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
