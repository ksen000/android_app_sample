package b07.example.flightbooking;

import java.util.*;
import data.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A class representing an activity where user chooses one Itinerary.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class ChooseItineraryActivity extends AdminMenuActivity {

    /** Search object that contains Itinerary search result. */
    private SearchItineraries searchI;

    /**
     * {@inheritDoc} Initializes controllingClientEditText,
     * which shows who is currently controlled by admin.
     * Initializes the SearchItinerary object, which holds the
     * Itinerary search result. Initializes ListView, which is
     * for displaying the search result onto the screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_itinerary);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);
        searchI = (SearchItineraries) superVisor.getSearchObject();
        setListView();
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_itinerary, menu);
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
     * Sorts the search result by cost and re-displays
     * the search result onto the screen.
     * @param view
     */
    public void sortByCost(View view) {
        searchI.sortSearchResultByCost();
        setListView();
    }

    /**
     * Sorts the search result by travel time and re-displays
     * the search result onto the screen.
     * @param view
     */
    public void sortByTravelTime(View view) {
        searchI.sortSearchResultByTime();
        setListView();
    }

    /**
     * Sets the search result onto ListView,
     * which is displayed on the screen.
     */
    private void setListView() {
        ArrayList<Itinerary> itineraries = searchI.getSearchedItinerary();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1);

        for (int i = 0; i < itineraries.size(); i++)
            adapter.add(itineraries.get(i).getItineraryNumber());

        ListView listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            public void onItemClick(
                    AdapterView<?> parent, View view, int pos, long id) {

                ListView listView = (ListView) parent;
                String itineNum = (String) listView.getItemAtPosition(pos);
                Intent intent = new Intent(ChooseItineraryActivity.this,
                        ShowItineraryInfoActivity.class);
                intent.putExtra(
                        MainActivity.ITINERARY_NUMBER_KEY, itineNum);
                startActivity(intent);
            }
        });
    }
}
