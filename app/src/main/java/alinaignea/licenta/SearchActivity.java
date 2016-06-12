package alinaignea.licenta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;

import alinaignea.licenta.helper.SQLiteHandler;
import alinaignea.licenta.menu_classes.ViewProfile;

/**
 * Created by Alina Ignea on 6/12/2016.
 */
public class SearchActivity extends Activity {

    private Button btnCancel, btnSearch;
    private RadioGroup free_seats;
    private EditText departure_time, edit_from, edit_to;
    String depart, seats, origin, destination;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSearch = (Button) findViewById(R.id.btnSearchTrips);
        free_seats = (RadioGroup) findViewById(R.id.free_seats);

        departure_time = (EditText) findViewById(R.id.departure_time);
        edit_from = (EditText) findViewById(R.id.edit_from);
        edit_to = (EditText) findViewById(R.id.edit_to);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                depart = departure_time.getText().toString();
                if (free_seats.getCheckedRadioButtonId() == -1)
                {
                    // no radio buttons are checked, consider all
                    seats="0";
                }
                else
                {
                    // one of the radio buttons is checked
                    // get selected radio button from radioGroup
                    int selectedId2 = free_seats.getCheckedRadioButtonId();
                    // find the radio button by returned id
                    RadioButton radioButton2 = (RadioButton) findViewById(selectedId2);
                    seats = radioButton2.getText().toString();
                }

                origin = edit_from.getText().toString();
                destination = edit_to.getText().toString();
                if(!depart.isEmpty())
                    if (!depart.matches("([01][0-9]|2[0-3]):[0-5][0-9]"))
                        Toast.makeText(getApplicationContext(), "Please insert a valid time in the format hh:mm!", Toast.LENGTH_LONG).show();
                if (!seats.isEmpty() && !origin.isEmpty()&& !destination.isEmpty())
                {
                    Intent i = new Intent(getApplicationContext(),
                            ShowSearchActivity.class);
                    i.putExtra("depart", depart);
                    i.putExtra("seats", seats);
                    i.putExtra("origin", origin);
                    i.putExtra("destination", destination);

                    startActivity(i);
                    finish();

                }

                else
                    Toast.makeText(getApplicationContext(),
                            "Please fill in your trip details!", Toast.LENGTH_LONG).show();
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
                startActivity(new Intent(this, ViewProfile.class));
                return true;
            case R.id.edit:
                return true;
            case R.id.main:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
