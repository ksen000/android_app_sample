package b07.example.flightbooking;

import data.*;
import managers.MainSystem;
import managers.AppSuperVisor;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class representing an admin menu of the flight booking application.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class AdminMenuActivity extends MainActivity {

    /** Shows who is currently controlled by admin. */
    protected TextView controllingClientEditText;

    /** MainSystem of this flight booking application. */
    protected MainSystem system;

    /** Super visor of MainSystem and this flight booking app. */
    protected AppSuperVisor superVisor;
    
    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows who is
     * currently controlled by admin. Sets AppSuperVisor and MainSystem.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_menu);
        superVisor = (AppSuperVisor) getApplicationContext();
        system = superVisor.getMainSystem();
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);
        setControlledClientMessage();
    }

    @Override
    /**
     * {@inheritDoc}
     * Sets controllingClientEditText, which shows
     * who is currently controlled by admin.
     */
    protected void onStart() {
        super.onStart();
        setControlledClientMessage();
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the
        // action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
     * Goes to UploadClientActivity.
     * @param view
     */
    public void uploadClient(View view) {
        Intent intent = new Intent(this, UploadClientActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to UploadFlightActivity.
     * @param view
     */
    public void uploadFlight(View view) {
        Intent intent = new Intent(this, UploadFlightActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to SearchClientActivity.
     * @param view
     */
    public void controlClient(View view) {
        Intent intent = new Intent(this, SearchClientActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to SearchClientActivity.
     * @param view
     */
    public void searchClient(View view) {
        Intent intent = new Intent(this, SearchClientActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to SearchFlightActivity.
     * @param view
     */
    public void searchFlight(View view) {
        Intent intent = new Intent(this, SearchFlightActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to SearchItineraryActivity.
     * @param view
     */
    public void searchItinerary(View view) {
        Intent intent = new Intent(this, SearchItineraryActivity.class);
        startActivity(intent);
    }

    /**
     * Logs out from this flight booking application
     * and goes to MainActivity.
     * @param view
     */
    public void logOut(View view) {
        // you have to implement the data upload program
        // inside this function.
        system.discardUserType();
        String msg = "Sucessfully logged out!";
        Toast.makeText(
                getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Sets who is currently controlled by admin onto TextView,
     * which is located at the bottom of the screen in each activity.
     */
    protected void setControlledClientMessage() {
        if (!system.getUserType().equals("admin"))
            return;
        String controlledClientStr;
        if (superVisor.noClientControlled()) {
            controlledClientStr = "Currently controlling: ";
            controlledClientStr += "No client is controlled.";
        } else {
            Client controlledClient = superVisor.getClientControlled();
            String email = controlledClient.getEmail();
            String firstname = controlledClient.getFirstname();
            String lastname = controlledClient.getLastname();
            controlledClientStr = "Currently controlling: ";
            controlledClientStr += email + ", " + firstname + " " + lastname;
        }
        controllingClientEditText.setText(controlledClientStr);
    }
    
    public void backToMenu(View view) {
        if (system.getUserType().equals("admin")) {
            Intent intent = new Intent(this, AdminMenuActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ClientMenuActivity.class);
            startActivity(intent);
        }
    }

}
