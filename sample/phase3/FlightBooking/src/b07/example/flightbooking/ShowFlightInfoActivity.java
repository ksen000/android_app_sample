package b07.example.flightbooking;

import java.text.DecimalFormat;
import data.Flight;
import data.InvalidTransactionException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class representing a Flight info displaying activity for admin.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class ShowFlightInfoActivity extends AdminMenuActivity {

    /** A flight whose info is displayed onto the screen. */
    private Flight flight;

    @Override
    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows
     * who is currently controlled by admin.
     * Also initializes the TextView, which displays the given
     * Flight data onto the screen.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_flight_info);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

        Intent intent = getIntent();
        String flightNum = (String) intent.getExtras().getSerializable(
                MainActivity.FLIGHT_NUMBER_KEY);

        try {
            flight = system.getFlight(flightNum);
        } catch (InvalidTransactionException e) {
            String msg = "No such Flight. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        TextView flightNumberEditText = (TextView) findViewById(
                R.id.flight_number_text_view);
        TextView departureDateTimeEditText = (TextView) findViewById(
                R.id.departure_datetime_text_view);
        TextView arrivalDateTimeEditText = (TextView) findViewById(
                R.id.arrival_datetime_text_view);
        TextView airlineEditText = (TextView) findViewById(
                R.id.airline_text_view);
        TextView originEditText = (TextView) findViewById(
                R.id.origin_text_view);
        TextView destinationEditText = (TextView) findViewById(
                R.id.destination_text_view);
        TextView costEditText = (TextView) findViewById(
                R.id.cost_text_view);
        TextView capacityEditText = (TextView) findViewById(
                R.id.capacity_text_view);

        DecimalFormat df = new DecimalFormat("#.00");
        String flightNumber = "Flight number: " + flight.getFlightNumber();
        String departureDateTime = "Departure: "
                + flight.getDepartureDateTime();
        String arrivalDateTime = "Arrival: " + flight.getArrivalDateTime();
        String airline = "Airline: " + flight.getAirline();
        String origin = "Origin: " + flight.getOrigin();
        String destination = "Destination: " + flight.getDestination();
        String cost = "Price: $" + df.format(flight.getCost());
        String capacity = "Capacity: ";
        capacity += Integer.toString(flight.getCapacity());

        flightNumberEditText.setText(flightNumber);
        departureDateTimeEditText.setText(departureDateTime);
        arrivalDateTimeEditText.setText(arrivalDateTime);
        airlineEditText.setText(airline);
        originEditText.setText(origin);
        destinationEditText.setText(destination);
        costEditText.setText(cost);
        capacityEditText.setText(capacity);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.show_flight_info, menu);
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
     * Goes to EditFlightActivity.
     * Passes the flight number of Flight you want to edit, via Intent.
     * @param view
     */
    public void editFlight(View view) {
        Intent intent = new Intent(this, EditFlightActivity.class);
        intent.putExtra(MainActivity.FLIGHT_NUMBER_KEY,
                flight.getFlightNumber());
        startActivity(intent);
    }

    /**
     * Goes to ChooseClientActivity.
     * Passes the list of passengers (Client) to this activity.
     * If this flight has no passengers, then display the toast message
     * saying "This flight has no passenger".
     * @param view
     */
    public void viewPassangers(View view) {
        if (flight.getPassengers().size() == 0) {
            String msg = "This flight has no passanger.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ChooseClientActivity.class);
        intent.putExtra(MainActivity.FLIGHT_NUMBER_KEY,
                flight.getFlightNumber());
        startActivity(intent);
    }

}
