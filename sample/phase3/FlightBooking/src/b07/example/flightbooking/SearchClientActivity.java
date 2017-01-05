package b07.example.flightbooking;

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
 * A class representing a client search activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class SearchClientActivity extends AdminMenuActivity {

    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText,
     * which shows who is currently controlled by admin.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_client);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);

    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_client, menu);
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
     * Gets Client object from the MainSystem by referencing
     * the given email.
     * Asks for the valid email if such Client doesn't exist.
     * Goes to ShowClientInfoActivity.
     * Passes email of the Client we've just created, by intent.
     * @param view
     */
    public void submitEmail(View view) {
        EditText emailEditText = (EditText) findViewById(
                R.id.email_edit_text);
        String email = emailEditText.getText().toString();
        try {
            system.getClient(email);
        } catch (InvalidTransactionException e) {
            String msg = e.getMessage();
            int idx = msg.indexOf("@");
            msg = msg.substring(0, idx);
            msg += ". Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, ShowClientInfoActivity.class);
        intent.putExtra(MainActivity.EMAIL_KEY, email);
        startActivity(intent);
    }
}
