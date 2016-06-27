package alinaignea.licenta;

/**
 * Created by Alina Ignea on 5/15/2016.
 */
import alinaignea.licenta.helper.SQLiteHandler;
import alinaignea.licenta.helper.SessionManager;
import alinaignea.licenta.login_register.LoginActivity;
import alinaignea.licenta.menu_profile.EditProfileActivity;
import alinaignea.licenta.menu_profile.ViewProfileActivity;
import alinaignea.licenta.trips.AddActivity;
import alinaignea.licenta.trips.SearchActivity;
import alinaignea.licenta.trips.ShowActivity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        btnAddTrip.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        AddActivity.class);
                startActivity(i);
                finish();

            }
        });

        btnViewTrips.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ShowActivity.class);
                startActivity(i);
                finish();

            }
        });

        btnSearchTrips.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        SearchActivity.class);
                startActivity(i);
                finish();

            }
        });

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

                startActivity(new Intent(MainActivity.this, ViewProfileActivity.class));
                return true;
            case R.id.edit:
                startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
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