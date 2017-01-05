package b07.example.flightbooking;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * A class representing a Flight info uploading activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class UploadFlightActivity extends AdminMenuActivity {

    @Override
    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows
     * who is currently controlled by admin.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_flight);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_flight, menu);
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
     * Goes to UploadFlightFromFileActivity.
     * @param view
     */
    public void uploadFlightFromFile(View view) {
        Intent intent = new Intent(
                this, UploadFlightFromFileActivity.class);
        startActivity(intent);
    }

    /**
     * Goes to UploadFlightManuallyActivity.
     * @param view
     */
    public void uploadFlightManually(View view) {
        Intent intent = new Intent(
                this, UploadFlightManuallyActivity.class);
        startActivity(intent);
    }

}
