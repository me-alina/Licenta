package alinaignea.licenta.menu_classes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import alinaignea.licenta.AppConfig;
import alinaignea.licenta.MainActivity;
import alinaignea.licenta.R;
import alinaignea.licenta.helper.SQLiteHandler;
import alinaignea.licenta.helper.SessionManager;

/**
 * Created by Alina Ignea on 5/19/2016.
 */
public class ViewProfile extends Activity {

    private Button btnEdit;
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtPhone;
    private TextView txtCity;
    private TextView txtAbout;
    private ProgressDialog pDialog;
    String myJSON;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


        btnEdit = (Button) findViewById(R.id.btnEdit);
        txtName = (TextView) findViewById(R.id.txtname);
        txtEmail = (TextView) findViewById(R.id.txtemail);
        txtPhone = (TextView) findViewById(R.id.txtphone);
        txtCity = (TextView) findViewById(R.id.txtcity);
        txtAbout = (TextView) findViewById(R.id.txtabout);


        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());


        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

       if (!email.isEmpty())
            getUserData(email);


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

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    protected void showData(String result){

        try {
            JSONObject jObj = new JSONObject(result);
            boolean error = jObj.getBoolean("error");

            // Check for error node in json
            if (!error) {
                // user profile successfully fetched

                // Now show profile
                String uid = jObj.getString("uid");

                JSONObject user = jObj.getJSONObject("user");

                String created_at = user.getString("created_at");
                String phone = user.getString("phone");
                //   String city = user.getString("city");
                //   String about = user.getString("about");
                //   String rating_cnt = user.getString("rating_cnt");
                //   String rating_val = user.getString("rating_val");


                txtPhone.setText(phone);
                //  txtCity.setText(city);
                //  txtAbout.setText(about);


            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void getUserData(final String email) {

            class GetDataJSON extends AsyncTask<String, Void, String>{

                @Override
                protected String doInBackground(String... params) {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(AppConfig.URL_PROFILE);

                    // Depends on your web service
                    httppost.setHeader("Content-type", "application/json");
                    try {
                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("email", email));
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

                    showData(result);

                }
            }
        GetDataJSON g = new GetDataJSON();
        g.execute();
        }

    }


