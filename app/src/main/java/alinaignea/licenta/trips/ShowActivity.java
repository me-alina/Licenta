package alinaignea.licenta.trips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import alinaignea.licenta.MainActivity;
import alinaignea.licenta.R;
import alinaignea.licenta.helper.AppConfig;
import alinaignea.licenta.helper.SQLiteHandler;
import alinaignea.licenta.menu_profile.EditProfileActivity;
import alinaignea.licenta.menu_profile.ViewOtherProfileActivity;
import alinaignea.licenta.menu_profile.ViewProfileActivity;

/**
 * Created by Alina Ignea on 6/11/2016.
 */
public class ShowActivity extends Activity {

    String myJSON;
    private SQLiteHandler db;
    private String passengerId;
    protected static final String TAG_RESULTS="response";
    protected static final String TAG_TRIPID = "id";
    protected static final String TAG_UID = "uid";
    protected static final String TAG_NAME = "name";
    protected static final String TAG_TIME = "depart";
    protected static final String TAG_ORIG = "origin";
    protected static final String TAG_DEST = "destination";
    protected static final String TAG_SEATS = "seats";
    protected static final String TAG_DATE = "date";



    JSONArray people = null;

    ArrayList<HashMap<String, String>> tripsList;

    ListView list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        list = (ListView) findViewById(R.id.listView);
        tripsList = new ArrayList<HashMap<String,String>>();
        getData();
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
                startActivity(new Intent(this, ViewProfileActivity.class));
                return true;
            case R.id.edit:
                startActivity(new Intent(this, EditProfileActivity.class));
                return true;
            case R.id.main:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost(AppConfig.URL_SHOW);

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

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
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            people = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i = 0; i< people.length(); i++){
                JSONObject c = people.getJSONObject(i);
                String tripid = c.getString(TAG_TRIPID);
                String uid = c.getString(TAG_UID);
                String name = c.getString(TAG_NAME);
                String time = c.getString(TAG_TIME);
                String date = c.getString(TAG_DATE);
                String orig = c.getString(TAG_ORIG);
                String dest = c.getString(TAG_DEST);
                String seats = c.getString(TAG_SEATS);
                String day=date.substring(date.length() - 2);
                String month = date.substring(5, 7);
                date = day + "/"+month;

                HashMap<String,String> trips = new HashMap<String,String>();
                trips.put(TAG_NAME,name);
                trips.put(TAG_TRIPID,tripid);
                trips.put(TAG_UID,uid);
                trips.put(TAG_TIME, "In " + date + ", at "+time);
                trips.put(TAG_ORIG,"From "+orig);
                trips.put(TAG_DEST,"To "+dest);
                trips.put(TAG_SEATS,seats+" free seats");

                tripsList.add(trips);
            }

            ListAdapter adapter = new SimpleAdapter(
                    ShowActivity.this, tripsList, R.layout.list_item,
                    new String[]{TAG_NAME, TAG_TIME, TAG_ORIG, TAG_DEST, TAG_SEATS},
                    new int[]{R.id.username, R.id.time, R.id.orig, R.id.dest, R.id.seats}
            );



            if (!tripsList.isEmpty()) {
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                            long arg3) {
                        // TODO Auto-generated method stub
                        //Log.d("############", "Items " + tripsList.get(arg2).get(TAG_UID));
                        joinTrip(tripsList.get(arg2).get(TAG_UID), tripsList.get(arg2).get(TAG_TRIPID));
                        final String driverUid = tripsList.get(arg2).get(TAG_UID);
                        new AlertDialog.Builder(ShowActivity.this)
                                .setTitle("Congratulations!")
                                .setMessage("We will let "+ tripsList.get(arg2).get(TAG_NAME) + " know you want to join. Would you like to see the user profile?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getApplicationContext(),
                                                ViewOtherProfileActivity.class);
                                        i.putExtra("driverId", driverUid);
                                        startActivity(i);
                                        finish();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // nimic
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                       // Toast.makeText(getApplicationContext(), tripsList.get(arg2).get(TAG_UID) , Toast.LENGTH_LONG).show();

                    }

                });
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Unfortunately there are no trips to show" , Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    private void joinTrip(final String driverId, final String tripId){
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        passengerId = user.get("uid");

        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(AppConfig.URL_JOIN);

                // Depends on your web service
                httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
                try {
                    ArrayList<NameValuePair> nameValuePairs;
                    nameValuePairs = new ArrayList<NameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("driver_uid", driverId));
                    nameValuePairs.add(new BasicNameValuePair("passenger_uid", passengerId));
                    nameValuePairs.add(new BasicNameValuePair("trip_uid", tripId));


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
                //    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();



    }


}
