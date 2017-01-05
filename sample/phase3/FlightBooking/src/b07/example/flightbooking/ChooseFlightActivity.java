package b07.example.flightbooking;

import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import data.SearchFlights;
import data.Flight;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A class representing an activity where user chooses one Flight.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class ChooseFlightActivity extends AdminMenuActivity {

    /** Search object that contains the Flight search result. */
    private SearchFlights searchf;

    /**
     * {@inheritDoc} Initializes controllingClientEditText,
     * which shows who is currently controlled by admin.
     * Initializes the SearchFlight object, which holds the
     * Flight search result. Initializes ListView, which is for
     * displaying the search result onto the screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_flight);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

        searchf = (SearchFlights) superVisor.getSearchObject();
        ArrayList<Flight> flights = searchf.getSearchedFlight();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        for (int i = 0; i < flights.size(); i++)
            adapter.add(flights.get(i).getFlightNumber());

        ListView listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            public void onItemClick(
                    AdapterView<?> parent, View view, int pos, long id) {

                ListView listView = (ListView) parent;
                String flightNum = (String) listView.getItemAtPosition(pos);
                Class<?> nextActivity = ShowFlightInfoActivity.class;
                if (!system.getUserType().equals("admin"))
                    nextActivity = ShowFlightInfoToClientActivity.class;
                Intent intent = new Intent(ChooseFlightActivity.this,
                        nextActivity);
                intent.putExtra(MainActivity.FLIGHT_NUMBER_KEY, flightNum);
                startActivity(intent);
            }
        });
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_flight, menu);
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

}
