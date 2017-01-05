package b07.example.flightbooking;

import java.util.*;
import java.text.DecimalFormat;
import data.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class representing a Itinerary info displaying activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class ShowItineraryInfoActivity extends AdminMenuActivity {

    /** An Itinerary whose info is displayed onto the screen. */
    private Itinerary itinerary;

    /** A client who is currently controlled by admin. */
    private Client client;
    
    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows who is
     * currently controlled by admin. Also initializes the TextView,
     * which displays the given Itinerary data onto the screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_itinerary_info);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

        if (!superVisor.noClientControlled())
            client = superVisor.getClientControlled();
        Intent intent = getIntent();
        String itineraryNum = (String) intent.getExtras().getSerializable(
                MainActivity.ITINERARY_NUMBER_KEY);

        try {
            itinerary = system.getItinerary(itineraryNum);
        } catch (InvalidTransactionException e) {
            String msg = "No such Flight. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        TextView itineraryNumberEditText = (TextView) findViewById(
                R.id.itinerary_number_text_view);
        TextView departureDateTimeEditText = (TextView) findViewById(
                R.id.departure_datetime_text_view);
        TextView arrivalDateTimeEditText = (TextView) findViewById(
                R.id.arrival_datetime_text_view);
        TextView originEditText = (TextView) findViewById(
                R.id.origin_text_view);
        TextView destinationEditText = (TextView) findViewById(
                R.id.destination_text_view);
        TextView costEditText = (TextView) findViewById(
                R.id.cost_text_view);
        TextView capacityEditText = (TextView) findViewById(
                R.id.capacity_text_view);
        TextView reservationSpaceEditText = (TextView) findViewById(
                R.id.space_text_view);

        DecimalFormat df = new DecimalFormat("#.00");
        String itineraryNumber = "Itinerary number: ";
        itineraryNumber += itinerary.getItineraryNumber();
        String departureDateTime = "Departure: ";
        departureDateTime += itinerary.getDepartureDateTime(); 
        String arrivalDateTime = "Arrival: ";
        arrivalDateTime += itinerary.getArrivalDateTime();
        String origin = "Origin: " + itinerary.getOrigin();
        String destination = "Destination: " + itinerary.getDestination();
        String cost = "Price: $" + df.format(itinerary.getTotalCost());
        String capacity = "Capacity: ";
        capacity += Integer.toString(itinerary.getCapacity());
        String reservationSpace = "Space: ";
        reservationSpace += Integer.toString(
                itinerary.getReservationSpace());

        itineraryNumberEditText.setText(itineraryNumber);
        departureDateTimeEditText.setText(departureDateTime);
        arrivalDateTimeEditText.setText(arrivalDateTime);
        originEditText.setText(origin);
        destinationEditText.setText(destination);
        costEditText.setText(cost);
        capacityEditText.setText(capacity);
        reservationSpaceEditText.setText(reservationSpace);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_itinerary_info, menu);
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
     * Goes to ChooseFlightActivity. Passes the list of flights in this
     * Itinerary, whose info is currently displayed.
     * @param view
     */
    public void viewFlight(View view) {
        SearchFlights searchF = new SearchFlights(system, "", "", "");
        ArrayList<Flight> flights = new ArrayList<Flight>();
        for (Flight f : itinerary.getFlights())
            flights.add(f);
        searchF.setSearchResult(flights);
        Intent intent = new Intent(this, ChooseFlightActivity.class);
        superVisor.setSearchObject(searchF);
        startActivity(intent);
    }

    /**
     * Adds this Itinerary, whose info is currently displayed in
     * this activity, to the shopping cart of Client, who is currently
     * controlled by admin/himself.
     * If you added the same Itinerary, then the error toast message
     * will show up.
     * @param view
     */
    public void addToShoppingCart(View view) {
        if (superVisor.noClientControlled()) {
            String msg = "You haven't decided who to control!";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            client.addItineraryToShoppingCart(itinerary);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx);
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = "Successfully added this itinerary to shopping cart!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Removes this Itinerary, whose info is currently displayed in this
     * activity, from the shopping cart of Client, who is currently
     * controlled by admin/himself.
     * If you removes the Itinerary that you haven't added before,
     * then the error toast message will show up.
     * @param view
     */
    public void removeFromShoppingCart(View view) {
        if (superVisor.noClientControlled()) {
            String msg = "You haven't decided who to control!";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            client.deleteItineraryFromShoppingCart(itinerary);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx);
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = "Successfully removed this itinerary ";
        msg += "from shopping cart!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Buys this Itinerary, whose info is currently displayed in
     * this activity, for the Client, who is currently controlled by
     * admin/himself. If you added the same Itinerary twice, then the error
     * toast message will popup.
     * @param view
     */
    public void bookItinerary(View view) {
        if (superVisor.noClientControlled()) {
            String msg = "You haven't decided who to control!";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            client.bookItinerary(itinerary);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx);
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = "Successfully booked this itinerary!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Cancels this Itinerary, whose info is currently displayed in this
     * activity, for the Client, who is currently controlled by
     * admin/himself.
     * If you try to cancel the itinerary that you haven't booked before,
     * then the error toast message will popup.
     * @param view
     */
    public void cancelItinerary(View view) {
        if (superVisor.noClientControlled()) {
            String msg = "You haven't decided who to control!";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            client.cancelBookedItinerary(itinerary);
        } catch (InvalidTransactionException e) {
            String msg = "You don't even have this itinerary.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = "Successfully canceled this itinerary!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();
    }
}
