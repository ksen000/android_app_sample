package b07.example.flightbooking;

import data.*;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import data.InvalidTransactionException;

/**
 * A class representing a login activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class LoginActivity extends MainActivity {

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
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
     * Checks if the given login info is valid or invalid.
     * If valid, then allow this Client/Admin into this
     * flight booking application. If invalid, then ask for the
     * email/password again.
     * Goes to ClientMenuActivity or AdminMenuActivity.
     * @param view
     */
    public void submitLoginInfo(View view) {
        EditText emailEditText = (EditText) findViewById(
                R.id.email_edit_text);
        EditText passwordEditText = (EditText) findViewById(
                R.id.password_edit_text);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        int loginResult;
        try {
            loginResult = system.checkLogin(email, password);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            msg = msg.substring(0, msg.indexOf("@"));
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        // invalid login
        if (loginResult == -1) {
            String msg = "Email or password is wrong. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
        }
        if (loginResult == 1) {
            String msg = "Successfully logged in!";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            system.setUserType(email);
            if (email.equals("admin")) {
                Intent intent = new Intent(this, AdminMenuActivity.class);
                startActivity(intent);
            } else {
                Client client;
                try {
                    client = system.getClient(email);
                } catch (InvalidTransactionException e) {
                    msg = "No such Client. Please try again.";
                    Toast.makeText(getApplicationContext(), msg,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                superVisor.setClientControlled(client);
                Intent intent = new Intent(this, ClientMenuActivity.class);
                startActivity(intent);
            }
        }
    }

}
