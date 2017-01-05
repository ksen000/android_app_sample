package b07.example.flightbooking;

import java.io.FileNotFoundException;
import java.io.IOException;
import data.InvalidTransactionException;
import data.Flight;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * A class representing a Flight info uploading, from file, activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class UploadFlightFromFileActivity extends AdminMenuActivity {

    @Override
    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows
     * who is currently controlled by admin.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_flight_from_file);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_flight_from_file, menu);
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
     * Gets the path of a file from the user.
     * From the Flight info contained in the given file,
     * it creates a new Flight object. Then uploads this Flight
     * object to MainSystem. If we couldn't find the file,
     * then toast message will popup.
     * @param view
     */
    public void submitPath(View view) {
        EditText pathEditText = (EditText) findViewById(
                R.id.path_edit_text);
        String path = pathEditText.getText().toString();
        FileReader myfile;
        try {
            myfile = new FileReader(path);
        } catch (FileNotFoundException e) {
            String msg = "No such file. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            BufferedReader br = new BufferedReader(myfile);
            String line;
            while ((line = br.readLine()) != null && line != "") {
                String tmpline = line.replace("\n", "");
                String[] s = tmpline.split(",");
                Flight f = new Flight(
                        s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7]);
                system.addFlightToDatabase(f);
            }
            br.close();
        } catch (IOException e) {
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        String msg = "Successfully uploaded the flight!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();
    }

}
