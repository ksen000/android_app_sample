package b07.example.flightbooking;

import java.util.ArrayList;
import data.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class representing a flight search activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class SearchFlightActivity extends AdminMenuActivity {

    /**
     * {@inheritDoc} Initializes controllingClientEditText,
     * which shows who is currently controlled by admin.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(
                R.menu.set_flight_search_criteria, menu);
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
     * Gets Flight object from the MainSystem by referencing
     * the given flight number.
     * Asks for the valid flight number if such Flight doesn't exist.
     * Goes to ShowFlightInfoActivity.
     * Passes flight number of the Flight we've just created, by intent.
     * @param view
     */
    public void submitFlightNumber(View view) {
        EditText flighNumberEditText = (EditText) findViewById(
                R.id.flight_number_edit_text);
        String flightNumber = flighNumberEditText.getText().toString();
        try {
            system.getFlight(flightNumber);
        } catch (InvalidTransactionException e) {
            String tmp = e.getMessage();
            String msg = tmp.substring(0, tmp.indexOf("@"));
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ShowFlightInfoActivity.class);
        intent.putExtra(MainActivity.FLIGHT_NUMBER_KEY, flightNumber);
        startActivity(intent);
    }

    /**
     * Searches Flight by referencing the given search criteria. Goes to
     * ChooseFlightActivity to show the search result. Asks for the search
     * criteria again, if we couldn't find the desired result.
     * @param view
     */
    public void submitFlightSearchCriteria(View view) {
        EditText originEditText = (EditText) findViewById(
                R.id.origin_edit_text);
        EditText destinationEditText = (EditText) findViewById(
                R.id.destination_edit_text);
        EditText departureDateEditText = (EditText) findViewById(
                R.id.departure_date_edit_text);

        String origin = originEditText.getText().toString();
        String destination = destinationEditText.getText().toString();
        String departureDate = departureDateEditText.getText().toString();

        SearchFlights searchf = new SearchFlights(system, departureDate,
                origin, destination);
        searchf.search();
        ArrayList<Flight> searchResult = searchf.getSearchedFlight();
        if (searchResult == null) {
            String msg = "Search is null.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        if (searchResult.size() == 0) {
            String msg = "No such flight. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        superVisor.setSearchObject(searchf);
        Intent intent = new Intent(this, ChooseFlightActivity.class);
        startActivity(intent);
    }
}
