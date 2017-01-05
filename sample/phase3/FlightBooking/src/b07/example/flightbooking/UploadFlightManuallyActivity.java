package b07.example.flightbooking;

import data.Flight;
import data.InvalidTransactionException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class representing a Flight info uploading, manually, activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class UploadFlightManuallyActivity extends AdminMenuActivity {

    @Override
    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows
     * who is currently controlled by admin.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_flight_manually);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_flight_manually, menu);
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
     * Given the manually typed Flight info, it uploads the new
     * Flight object to MainSystem.
     * If the input values are invalid, then we ask for another
     * input value.
     */
    public void uploadFlight(View view) {
        // get the user inputs
        EditText flightNumberEditText = (EditText) findViewById(
                R.id.flight_number_edit_text);
        EditText deptEditText = (EditText) findViewById(
                R.id.departure_datetime_edit_text);
        EditText arrivalEditText = (EditText) findViewById(
                R.id.arrival_datetime_edit_text);
        EditText airlineEditText = (EditText) findViewById(
                R.id.airline_edit_text);
        EditText originEditText = (EditText) findViewById(
                R.id.origin_edit_text);
        EditText destinationEditText = (EditText) findViewById(
                R.id.destination_edit_text);
        EditText priceEditText = (EditText) findViewById(
                R.id.price_edit_text);
        EditText capacityEditText = (EditText) findViewById(
                R.id.capacity_edit_text);

        String flightNum = flightNumberEditText.getText().toString();
        String departTime = deptEditText.getText().toString();
        String arrivalTime = arrivalEditText.getText().toString();
        String airline = airlineEditText.getText().toString();
        String origin = originEditText.getText().toString();
        String destination = destinationEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String capacity = capacityEditText.getText().toString();

        boolean flag = false;
        if (flightNum.length() == 0)
            flag = true;
        if (departTime.length() == 0)
            flag = true;
        if (arrivalTime.length() == 0)
            flag = true;
        if (airline.length() == 0)
            flag = true;
        if (origin.length() == 0)
            flag = true;
        if (destination.length() == 0)
            flag = true;
        if (price.length() == 0)
            flag = true;
        if (capacity.length() == 0)
            flag = true;
        if (flag) {
            String msg = "Please fill every row before uploading.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Flight flight = new Flight(flightNum, departTime, arrivalTime,
                    airline, origin, destination, price, capacity);
            system.addFlightToDatabase(flight);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx);
            msg += ". Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = "You have successfully uploaded a Flight!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(
                this, UploadFlightManuallyActivity.class);
        startActivity(intent);
    }

}