package b07.example.flightbooking;

import java.text.DecimalFormat;
import data.InvalidTransactionException;
import data.Flight;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class representing an activity where user edits Flight info.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class EditFlightActivity extends AdminMenuActivity {

    /** The flight who is going be edited. */
    private Flight flight;

    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows who is
     * currently controlled by admin. Also initializes all the EditText,
     * which is for editing flight info.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flight);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

        Intent intent = getIntent();
        String flightNum = (String) intent.getExtras().getSerializable(
                MainActivity.FLIGHT_NUMBER_KEY);
        try {
            flight = system.getFlight(flightNum);
        } catch (InvalidTransactionException e) {
            String msg = "No such Flight. Please try again.";
            Toast.makeText(
                    getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        EditText flightNumberEditText = (EditText) findViewById(
                R.id.flight_number_edit_text);
        EditText departureDateTimeEditText = (EditText) findViewById(
                R.id.departure_datetime_edit_text);
        EditText arrivalDateTimeEditText = (EditText) findViewById(
                R.id.arrival_datetime_edit_text);
        EditText airlineEditText = (EditText) findViewById(
                R.id.airline_edit_text);
        EditText originEditText = (EditText) findViewById(
                R.id.origin_edit_text);
        EditText destinationEditText = (EditText) findViewById(
                R.id.destination_edit_text);
        EditText costEditText = (EditText) findViewById(
                R.id.cost_edit_text);
        EditText capacityEditText = (EditText) findViewById(
                R.id.capacity_edit_text);

        DecimalFormat df = new DecimalFormat("#.00");
        String flightNumber = "Flight number: " + flight.getFlightNumber();
        String departureDateTime = "Can't change departure date & time";
        String arrivalDateTime = "Can't change arrival date & time";
        String airline = "Airline: " + flight.getAirline();
        String origin = "Can't change origin";
        String destination = "Can't change destination";
        String cost = "Price: " + df.format(flight.getCost());
        String capacity = "Capacity: ";
        capacity += Integer.toString(flight.getCapacity());

        flightNumberEditText.setHint(flightNumber);
        departureDateTimeEditText.setText(departureDateTime);
        arrivalDateTimeEditText.setText(arrivalDateTime);
        airlineEditText.setHint(airline);
        originEditText.setText(origin);
        destinationEditText.setText(destination);
        costEditText.setHint(cost);
        capacityEditText.setHint(capacity);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_flight, menu);
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
     * Takes in the input values from the screen and puts all the given info
     * into Flight object. Only changes what was modified by the user.
     * @param view
     */
    public void submitEditedInfo(View view) {
        EditText flightNumberEditText = (EditText) findViewById(
                R.id.flight_number_edit_text);
        EditText departureDateTimeEditText = (EditText) findViewById(
                R.id.departure_datetime_edit_text);
        EditText arrivalDateTimeEditText = (EditText) findViewById(
                R.id.arrival_datetime_edit_text);
        EditText airlineEditText = (EditText) findViewById(
                R.id.airline_edit_text);
        EditText originEditText = (EditText) findViewById(
                R.id.origin_edit_text);
        EditText destinationEditText = (EditText) findViewById(
                R.id.destination_edit_text);
        EditText costEditText = (EditText) findViewById(
                R.id.cost_edit_text);
        EditText capacityEditText = (EditText) findViewById(
                R.id.capacity_edit_text);

        String flightNumber = flightNumberEditText.getText().toString();
        String departureDateTime = flight.getDepartureDateTime();
        String arrivalDateTime = flight.getArrivalDateTime();
        String airline = airlineEditText.getText().toString();
        String origin = flight.getOrigin();
        String destination = flight.getDestination();
        String cost = costEditText.getText().toString();
        String capacity = capacityEditText.getText().toString();
        if (flightNumber.length() == 0)
            flightNumber = flight.getFlightNumber();
        if (airline.length() == 0)
            airline = flight.getAirline();
        if (cost.length() == 0)
            cost = Double.toString(flight.getCost());
        if (capacity.length() == 0)
            capacity = Integer.toString(flight.getCapacity());

        String prevFlightNumber = flight.getFlightNumber();
        flight.setFlightNumber(flightNumber);
        flight.setDepartureDateTime(departureDateTime);
        flight.setArrivalDateTime(arrivalDateTime);
        flight.setAirline(airline);
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setCost(Double.parseDouble(cost));
        flight.setCapacity(Integer.parseInt(capacity));
        try {
            system.deleteFlightFromDatabase(prevFlightNumber);
            system.addFlightToDatabase(flight);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        String msg = "Successfully changed the flight info!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();
    }
}
