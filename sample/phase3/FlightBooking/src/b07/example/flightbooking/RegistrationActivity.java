package b07.example.flightbooking;

import data.InvalidTransactionException;
import data.Client;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A class representing a registration activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class RegistrationActivity extends MainActivity {

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registration, menu);
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
     * Takes in all the Client info.
     * Creates a new Client object.
     * Puts new Client into the MainSystem.
     * Keeps asking for the valid input value, if they are not valid.
     * 
     * @param view
     */
    public void registerClient(View view) {

        // get the user input
        EditText emailEditText = (EditText) findViewById(
                R.id.email_edit_text);
        EditText passwordEditText = (EditText) findViewById(
                R.id.password_edit_text);
        EditText firstnameEditText = (EditText) findViewById(
                R.id.firstname_edit_text);
        EditText lastnameEditText = (EditText) findViewById(
                R.id.lastname_edit_text);
        EditText addressEditText = (EditText) findViewById(
                R.id.address_edit_text);
        EditText creditCardNumberEditText = (EditText) findViewById(
                R.id.credit_card_number_edit_text);
        EditText expiryDateEditText = (EditText) findViewById(
                R.id.expiry_date_edit_text);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String firstname = firstnameEditText.getText().toString();
        String lastname = lastnameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String ccNumber = creditCardNumberEditText.getText().toString();
        String expiryDate = expiryDateEditText.getText().toString();

        boolean flag = false;
        if (email.length() == 0)
            flag = true;
        if (password.length() == 0)
            flag = true;
        if (firstname.length() == 0)
            flag = true;
        if (lastname.length() == 0)
            flag = true;
        if (address.length() == 0)
            flag = true;
        if (ccNumber.length() == 0)
            flag = true;
        if (expiryDate.length() == 0)
            flag = true;
        if (flag) {
            String msg = "Please fill every row before registering.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Client c = new Client(lastname, firstname, email, address,
                    ccNumber, expiryDate);
            c.setPassword(password);
            system.addClientToDatabase(c);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx - 1);
            msg += ". Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        String msg = "You have successfully registered!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
