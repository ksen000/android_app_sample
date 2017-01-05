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
 * A class representing the itinerary search activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class SearchItineraryActivity extends AdminMenuActivity {

    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText,
     * which shows who is currently controlled by admin.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_itinerary);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_itinerary, menu);
        return true;
    }

    /** {@inheritDoc} */
    @Override
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
     * Gets Itinerary object from the MainSystem by referencing the given
     * itinerary number.
     * Asks for the valid itinerary number if such Itinerary doesn't exist.
     * Goes to ShowItineraryInfoActivity.
     * Passes itinerary number of the Itinerary we've
     * just created, by intent.
     * @param view
     */
    public void submitItineraryNumber(View view) {
        EditText iNumberEditText = (EditText) findViewById(
                R.id.itinerary_number_edit_text);
        String itineraryNumber = iNumberEditText.getText().toString();
        try {
            system.getItinerary(itineraryNumber);
        } catch (InvalidTransactionException e) {
            String msg = "No such itinerary. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ShowItineraryInfoActivity.class);
        intent.putExtra(MainActivity.ITINERARY_NUMBER_KEY, itineraryNumber);
        startActivity(intent);
    }

    /**
     * Searches Itinerary by referencing the given search criteria.
     * Goes to ChooseItineraryActivity to show the search result.
     * Asks for the search criteria again, if we couldn't find
     * the desired result.
     * 
     * @param view
     */
    public void submitItinerarySearchCriteria(View view) {
        EditText originEditText = (EditText) findViewById(
                R.id.origin_edit_text);
        EditText destinationEditText = (EditText) findViewById(
                R.id.destination_edit_text);
        EditText departureDateEditText = (EditText) findViewById(
                R.id.departure_date_edit_text);

        String origin = originEditText.getText().toString();
        String destination = destinationEditText.getText().toString();
        String departureDate = departureDateEditText.getText().toString();

        SearchItineraries searchI = new SearchItineraries(system,
                departureDate, origin, destination);
        try {
            searchI.search();
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx);
            msg += ". Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        ArrayList<Itinerary> searchResult = searchI.getSearchedItinerary();
        if (searchResult == null) {
            String msg = "Search is null.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_LONG).show();
            return;
        }
        if (searchResult.size() == 0) {
            String msg = "No such itinerary. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        superVisor.setSearchObject(searchI);
        Intent intent = new Intent(this, ChooseItineraryActivity.class);
        startActivity(intent);
    }
}
