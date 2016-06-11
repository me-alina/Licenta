package alinaignea.licenta;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import alinaignea.licenta.helper.SQLiteHandler;
import alinaignea.licenta.helper.SessionManager;
import alinaignea.licenta.menu_classes.EditProfile;
import alinaignea.licenta.menu_classes.ViewProfile;

/**
 * Created by Alina Ignea on 6/9/2016.
 */
public class AddActivity extends Activity {

    private Button btnCancel, btnAdd;
    private RadioGroup departure, free_seats;
    private RadioButton inserted_time;
    private EditText departure_time, edit_from, edit_to;
    String depart, seats, origin, destination;
    private SQLiteHandler db;
    private String uid;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        departure = (RadioGroup) findViewById(R.id.departure);
        inserted_time = (RadioButton) findViewById(R.id.radioButton4);
        free_seats = (RadioGroup) findViewById(R.id.free_seats);

        departure_time = (EditText) findViewById(R.id.departure_time);
        edit_from = (EditText) findViewById(R.id.edit_from);
        edit_to = (EditText) findViewById(R.id.edit_to);


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        uid = user.get("uid");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (departure.getCheckedRadioButtonId() == -1)
                {
                    // no radio buttons are checked
                    Toast.makeText(getApplicationContext(), "Please set your departure time!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    // one of the radio buttons is checked
                    // get selected radio button from radioGroup
                    int selectedId1 = departure.getCheckedRadioButtonId();
                    // find the radio button by returned id
                    RadioButton radioButton1 = (RadioButton) findViewById(selectedId1);
                    if(!radioButton1.equals(inserted_time))
                        depart = radioButton1.getText().toString();
                    else
                        depart = departure_time.getText().toString();
                }

                if (free_seats.getCheckedRadioButtonId() == -1)
                {
                    // no radio buttons are checked
                    Toast.makeText(getApplicationContext(), "Please set the number of free seats!", Toast.LENGTH_LONG).show();
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


                if (!depart.isEmpty() && !seats.isEmpty() && !origin.isEmpty()&& !destination.isEmpty())
                     addTrip(uid, depart, seats, origin, destination);
                //Toast.makeText(getApplicationContext(),
                      //  "Details complete!", Toast.LENGTH_LONG).show();

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
                startActivity(new Intent(this, EditProfile.class));
                return true;
            case R.id.logout:
                // to make public static method from logout method in class MainActivity!!!
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addTrip(final String uid, final String depart, final String seats, final String origin, final String destination)
    {

        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConfig.URL_ADD);

                // Depends on your web service
                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                try {
                    ArrayList<NameValuePair> nameValuePairs;
                    nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("uid", uid));
                    nameValuePairs.add(new BasicNameValuePair("time", depart));
                    nameValuePairs.add(new BasicNameValuePair("fromplace", origin));
                    nameValuePairs.add(new BasicNameValuePair("toplace", destination));
                    nameValuePairs.add(new BasicNameValuePair("seats", seats));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                } catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                    Log.d("php", result);
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){

                try {
                    JSONObject jObj = new JSONObject(result);
                    boolean error = jObj.getBoolean("error");
                    String errorMsg = jObj.getString("message");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();

                    // Check for error node in json
                    if (!error) {
                        // user profile successfully updated
                        // Now show profile
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }

                    else {
                        // Error in updating. Get the error message


                    }
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }




}
