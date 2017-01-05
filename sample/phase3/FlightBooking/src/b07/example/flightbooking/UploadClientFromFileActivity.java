package b07.example.flightbooking;

import java.io.FileNotFoundException;
import java.io.IOException;
import data.Client;
import data.InvalidTransactionException;
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
 * A class representing a Client info uploading, from file, activity.
 * 
 * @author Azi Zheng
 * @author Keita Senda
 * @author Kristofer Selinis
 */
public class UploadClientFromFileActivity extends AdminMenuActivity {

    @Override
    /**
     * {@inheritDoc}
     * Initializes controllingClientEditText, which shows
     * who is currently controlled by admin.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_client_from_file);
        controllingClientEditText = (TextView) findViewById(
                R.id.controlling_client_text_view);
    }

    @Override
    /** {@inheritDoc} */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upload_client_from_file, menu);
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
     * From the Client info contained in the given file,
     * it creates a new Client object. Then uploads this Client object to
     * MainSystem. If we couldn't find the file, then toast message
     * will popup.
     * @param view
     */
    public void submitPath(View view) {
        EditText pathEditText = (EditText) findViewById(R.id.path_edit_text);
        String path = pathEditText.getText().toString();
        String path2 = path.replace("client.txt", "") + "passwords.txt";
        FileReader clientInfoFile;
        FileReader passwordFile;
        try {
            clientInfoFile = new FileReader(path);
            passwordFile = new FileReader(path2);
        } catch (FileNotFoundException e) {
            String msg = "No such file. Please try again.";
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            
            // uploading client info (data are from client.txt)
            BufferedReader br = new BufferedReader(clientInfoFile);
            String line;
            while ((line = br.readLine()) != null && line != "") {
                String tmpline = line.replace("\n", "");
                String[] s = tmpline.split(",");
                Client c = new Client(s[0], s[1], s[2], s[3], s[4], s[5]);
                system.addClientToDatabase(c);
            }

            // setting password (data are from passwords.txt)
            br = new BufferedReader(passwordFile);
            String line2;
            while ((line2 = br.readLine()) != null && line2 != "") {
                String tmpline = line2.replace("\n", "");
                String[] s = tmpline.split(",");
                Client c = system.getClient(s[0]);
                c.setPassword(s[1]);
            }
            br.close();
        } catch (IOException e) {
            String msg = e.getMessage();
            Toast.makeText(getApplicationContext(),
                    msg, Toast.LENGTH_SHORT).show();
            return;
        } catch (InvalidTransactionException e) {
            System.out.println(e.getMessage());
        }

        String msg = "Successfully uploaded the client!";
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_SHORT).show();
    }

}
