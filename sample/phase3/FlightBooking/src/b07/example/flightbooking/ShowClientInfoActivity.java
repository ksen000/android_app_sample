package b07.example.flightbooking;

import java.util.*;
import data.Flight;
import data.Itinerary;
import data.SearchFlights;
import data.Client;
import data.InvalidTransactionException;
import data.SearchItineraries;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class representing a Client info displaying activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class ShowClientInfoActivity extends AdminMenuActivity {

    /** A client whose info is displayed onto the screen. */
    private Client client;

    @Override
    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows
     * who is currently controlled by admin.
     * Also initializes the TextView, which displays the given
     * Client data onto the screen.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_client_info);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

        Intent intent = getIntent();
        String email = (String) intent.getExtras().getSerializable(
                MainActivity.EMAIL_KEY);

        try {
            client = system.getClient(email);
        } catch (InvalidTransactionException e) {
            String msg = "No such Flight. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        TextView lastnameEditText = (TextView) findViewById(
                R.id.lastname_text_view);
        TextView firstnameEditText = (TextView) findViewById(
                R.id.firstname_text_view);
        TextView emailEditText = (TextView) findViewById(
                R.id.email_text_view);
        TextView addressEditText = (TextView) findViewById(
                R.id.address_text_view);
        TextView ccNumberEditText = (TextView) findViewById(
                R.id.ccnumber_text_view);
        TextView expiryDateEditText = (TextView) findViewById(
                R.id.expiry_date_text_view);

        String lastname = "Lastname: " + client.getLastname();
        String firstname = "Firstname: " + client.getFirstname();
        String curEmail = "Email: " + client.getEmail();
        String address = "Address: " + client.getAddress();
        String ccNumber = "Credit card number: ";
        ccNumber += client.getCreditCardNumber();
        String expiryDate = "Expiry date of credit card: ";
        expiryDate += client.getExpiryDate();

        lastnameEditText.setText(lastname);
        firstnameEditText.setText(firstname);
        emailEditText.setText(curEmail);
        addressEditText.setText(address);
        ccNumberEditText.setText(ccNumber);
        expiryDateEditText.setText(expiryDate);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_client_info, menu);
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
     * Passes email of the Client you want to edit.
     * @param view
     */
    public void editClient(View view) {
        Intent intent = new Intent(this, EditClientActivity.class);
        intent.putExtra(MainActivity.EMAIL_KEY, client.getEmail());
        startActivity(intent);
    }

    /**
     * Sets the Client controlled by admin to be the client whose info is
     * currently displayed on this activity.
     * @param view
     */
    public void controlThisClient(View view) {
        controlThisClient();
    }

    /**
     * Goes to ChooseItineraryActivity.
     * Passes the list of booked itineraries of this Client whose info 
     * is currently displayed on this activity,
     * @param view
     */
    public void viewBookedItinerary(View view) {
        if (client.getBookedItineraries().size() == 0) {
            String msg = "This client has no itinerary";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        Client client2 = superVisor.getClientControlled();
        if (superVisor.noClientControlled()
                || !client.getEmail().equals(client2.getEmail())) {
            controlThisClient();
            controlMessagePopup();
        }
        TreeSet<Itinerary> booked = client.getBookedItineraries();
        ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
        for (Itinerary e : booked)
            itineraries.add(e);
        SearchItineraries searchI = new SearchItineraries(
                system, "", "", "");
        searchI.setSearchResult(itineraries);

        superVisor.setSearchObject(searchI);
        Intent intent = new Intent(this, ChooseItineraryActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to ChooseItineraryActivity.
     * Passes the list of itineraries shopping cart of this Client
     * whose info is currently displayed on this activity.
     * @param view
     */
    public void viewShoppingCart(View view) {
        if (client.getShoppingCart().size() == 0) {
            String msg = "There's nothing inside his shopping cart!";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        Client client2 = superVisor.getClientControlled();
        if (superVisor.noClientControlled()
                || !client.getEmail().equals(client2.getEmail())) {
            controlThisClient();
            controlMessagePopup();
        }
        TreeSet<Itinerary> shoppingCart = client.getShoppingCart();
        ArrayList<Itinerary> itineraries = new ArrayList<Itinerary>();
        for (Itinerary e : shoppingCart)
            itineraries.add(e);
        SearchItineraries searchI = new SearchItineraries(
                system, "", "", "");
        searchI.setSearchResult(itineraries);

        superVisor.setSearchObject(searchI);
        Intent intent = new Intent(this, ChooseItineraryActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to SearchItineraryActivity.
     * You will start searching the itinerary from this method.
     * If the "Client controlled by admin" is null or different,
     * then set this to the Client whose info is currently displayed
     * on this activity.
     * @param view
     */
    public void addItinerary(View view) {
        Client client2 = superVisor.getClientControlled();
        if (superVisor.noClientControlled()
                || !client.getEmail().equals(client2.getEmail())) {
            controlThisClient();
            controlMessagePopup();
        }
        Intent intent = new Intent(this, SearchItineraryActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to ChooseFlightActivity.
     * Passes the list of flights this Client booked.
     * @param view
     */
    public void viewBookedFlight(View view) {
        if (client.getBookedItineraries().size() == 0) {
            String msg = "This client has no flights!";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        SearchFlights searchF = new SearchFlights(system, "", "", "");
        ArrayList<Flight> flights = new ArrayList<Flight>();
        for (Itinerary e : client.getBookedItineraries())
            for (Flight f : e.getFlights())
                flights.add(f);
        searchF.setSearchResult(flights);
        Intent intent = new Intent(this, ChooseFlightActivity.class);
        superVisor.setSearchObject(searchF);
        startActivity(intent);
    }

    /**
     * Sets this Client to be the Client who is controlled by admin.
     * Sets the message displayed on the bottom of the screen
     * in each activity.
     * Displays the toast message.
     */
    private void controlThisClient() {
        superVisor.setClientControlled(client);
        setControlledClientMessage();
        controlMessagePopup();
    }

    /**
     * Displays the message saying: "You are now controlling this client".
     */
    private void controlMessagePopup() {
        if (system.getUserType().equals("admin")) {
            String msg = "You are now controling this client";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
        }
    }
}
