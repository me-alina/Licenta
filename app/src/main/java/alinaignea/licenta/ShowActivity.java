package alinaignea.licenta;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Alina Ignea on 6/11/2016.
 */
public class ShowActivity extends Activity {

    String myJSON;

    private static final String TAG_RESULTS="response";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "depart";
    private static final String TAG_ORIG = "origin";
    private static final String TAG_DEST = "destination";
    private static final String TAG_SEATS = "seats";

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> tripsList;

    ListView list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        list = (ListView) findViewById(R.id.listView);
        tripsList = new ArrayList<HashMap<String,String>>();
        getData();
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
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String time = c.getString(TAG_TIME);
                String orig = c.getString(TAG_ORIG);
                String dest = c.getString(TAG_DEST);
                String seats = c.getString(TAG_SEATS);



                HashMap<String,String> trips = new HashMap<String,String>();
                trips.put(TAG_NAME,name);
                trips.put(TAG_TIME,"Leaving in "+time);
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

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
