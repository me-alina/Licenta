package alinaignea.licenta;

/**
 * Created by Alina Ignea on 5/15/2016.
 */
import alinaignea.licenta.helper.SQLiteHandler;
import alinaignea.licenta.helper.SessionManager;
import alinaignea.licenta.menu_classes.EditProfile;
import alinaignea.licenta.menu_classes.ViewProfile;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnViewTrips;
    private Button btnSearchTrips;
    private Button btnAddTrip;

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnViewTrips = (Button) findViewById(R.id.btnViewTrips);
        btnSearchTrips = (Button) findViewById(R.id.btnSearchTrips);
        btnAddTrip = (Button) findViewById(R.id.btnAddTrip);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.view:

                startActivity(new Intent(MainActivity.this, ViewProfile.class));
                return true;
            case R.id.edit:
                startActivity(new Intent(MainActivity.this, EditProfile.class));
                return true;
            case R.id.logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    public void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}