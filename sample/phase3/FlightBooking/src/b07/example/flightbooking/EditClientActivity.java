package b07.example.flightbooking;

import java.io.FileNotFoundException;
import java.io.IOException;
import data.InvalidTransactionException;
import data.Client;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A class representing an activity where user edits Client info.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class EditClientActivity extends AdminMenuActivity {

    /** The client who is going to be edited. */
    private Client client;

    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows who is
     * currently controlled by admin. Also initializes all the EditText,
     * which is for editing client info.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

        Intent intent = getIntent();
        String email = (String) intent.getExtras().getSerializable(
                MainActivity.EMAIL_KEY);
        try {
            client = system.getClient(email);
        } catch (InvalidTransactionException e) {
            String msg = "No such client. Please try again.";
            Toast.makeText(
                    getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

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

        String emailAddress = "Email address: " + client.getEmail();
        String password = "Password: " + client.getPassword();
        String firstname = "First name: " + client.getFirstname();
        String lastname = "Last name: " + client.getLastname();
        String address = "Address: " + client.getAddress();
        String ccNumber = "Credit card number: ";
        ccNumber += client.getCreditCardNumber();
        String expiryDate = "Expiry date: " + client.getExpiryDate();

        emailEditText.setHint(emailAddress);
        passwordEditText.setHint(password);
        firstnameEditText.setHint(firstname);
        lastnameEditText.setHint(lastname);
        addressEditText.setHint(address);
        creditCardNumberEditText.setHint(ccNumber);
        expiryDateEditText.setHint(expiryDate);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_client, menu);
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
     * Takes in the input values from the screen and puts all the given info
     * into Client object. Only changes what was modified by the user.
     * @param view
     */
    public void submitEditedInfo(View view) {
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

        String newEmail = emailEditText.getText().toString();
        String newPassword = passwordEditText.getText().toString();
        String newFirstname = firstnameEditText.getText().toString();
        String newLastname = lastnameEditText.getText().toString();
        String newAddress = addressEditText.getText().toString();
        String newCCNumber = creditCardNumberEditText.getText().toString();
        String newExpiryDate = expiryDateEditText.getText().toString();
        if (newEmail.length() == 0)
            newEmail = client.getEmail();
        if (newPassword.length() == 0)
            newPassword = client.getPassword();
        if (newFirstname.length() == 0)
            newFirstname = client.getFirstname();
        if (newLastname.length() == 0)
            newLastname = client.getLastname();
        if (newAddress.length() == 0)
            newAddress = client.getAddress();
        if (newCCNumber.length() == 0)
            newCCNumber = client.getCreditCardNumber();
        if (newExpiryDate.length() == 0)
            newExpiryDate = client.getExpiryDate();

        String prevEmail = client.getEmail();

        if (!prevEmail.equals(newEmail)
                && system.containsClient(newEmail)) {
            String msg = "This email is already taken by other user.";
            Toast.makeText(
                    getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!system.getUserType().equals("admin"))
            system.setUserType(newEmail);

        client.setEmail(newEmail);
        client.setPassword(newPassword);
        client.setFirstname(newFirstname);
        client.setLastname(newLastname);
        client.setAddress(newAddress);
        client.setCreditCardNumber(newCCNumber);
        client.setExpiryDate(newExpiryDate);
        try {
            system.deleteClientFromDatabase(prevEmail);
            system.addClientToDatabase(client);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            msg = msg.substring(0, msg.indexOf("@"));
            Toast.makeText(
                    getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        String msg = "Successfully changed the personal info!";
        Toast.makeText(
                getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();
    }
}
