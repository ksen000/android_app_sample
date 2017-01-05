package b07.example.flightbooking;

import java.util.*;
import data.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A class representing an activity where user chooses one Client info.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class ChooseClientActivity extends AdminMenuActivity {

    /** Flight object that contains all the passengers (Client). */
    private Flight flight;

    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows who is
     * currently controlled by admin. Initializes ListView, which is for
     * displaying the search result onto the screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_client);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

        Intent intent = getIntent();
        String flightNum = (String) intent.getExtras().getSerializable(
                MainActivity.FLIGHT_NUMBER_KEY);
        try {
            flight = system.getFlight(flightNum);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            msg = msg.substring(0, msg.indexOf("@"));
            Toast.makeText(
                    getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        TreeSet<Client> tmp = flight.getPassengers();
        ArrayList<Client> clients = new ArrayList<Client>();
        for (Client c : tmp)
            clients.add(c);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1);

        for (int i = 0; i < clients.size(); i++)
            adapter.add(clients.get(i).getEmail());

        ListView listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
            public void onItemClick(
                    AdapterView<?> parent, View view, int pos, long id) {

                ListView listView = (ListView) parent;
                String email = (String) listView.getItemAtPosition(pos);
                Intent intent = new Intent(ChooseClientActivity.this,
                        ShowClientInfoActivity.class);
                intent.putExtra(MainActivity.EMAIL_KEY, email);
                startActivity(intent);
            }
        });

    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choose_client, menu);
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
