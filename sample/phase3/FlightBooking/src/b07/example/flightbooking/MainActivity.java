package b07.example.flightbooking;

import managers.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

/**
 * A class representing the first screen of this flightbooking application.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class MainActivity extends ActionBarActivity {

    /** A key for passing email to another activity. */
    public static final String EMAIL_KEY = "emailKey";

    /** A key for passing flight number to another activity. */
    public static final String FLIGHT_NUMBER_KEY = "flightNumberKey";

    /** A key for passing itinerary number to another activity. */
    public static final String ITINERARY_NUMBER_KEY = "itineraryNumberKey";

    /** A key for passing activity type to another activity. */
    public static final String ACTIVITY_TYPE_KEY = "activityTypeKey";

    /** A main system of this flight booking application. */
    protected MainSystem system;

    /** A super visor of MainSystem and this app. */
    protected AppSuperVisor superVisor;

    /**
     * {@inheritDoc}
     * Initializes MainSystem and AppSuperVisor.
     * Uploads all the permanent data into this flight booking app.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        superVisor = (AppSuperVisor) getApplicationContext();
        system = superVisor.getMainSystem();
        try {
            superVisor.uploadPermanentData();
        } catch (IOException e) {
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            superVisor.savePermanentData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
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
     * Goes to LoginActivity.
     * @param view
     */
    public void goTologinPage(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to RegistrationActivity.
     * @param view
     */
    public void goToRegistrationPage(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    /**
     * Goes back to previous activity.
     * @param view
     */
    public void goBack(View view) {
        super.onBackPressed();
    }
}
