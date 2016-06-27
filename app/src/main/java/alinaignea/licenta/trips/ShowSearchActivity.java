package alinaignea.licenta.trips;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import alinaignea.licenta.MainActivity;
import alinaignea.licenta.R;

/**
 * Created by Alina Ignea on 6/12/2016.
 */
public class ShowSearchActivity extends ShowActivity {

    String depart, needed_seats, origin, destination, needed_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        depart = b.getString("depart");
        needed_seats = b.getString("seats");
        origin = b.getString("origin");
        destination = b.getString("destination");
        needed_date = b.getString("date");
    }

    @Override
    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            people = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i = 0; i< people.length(); i++){
                JSONObject c = people.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String uid = c.getString(TAG_UID);
                String time = c.getString(TAG_TIME);
                String date = c.getString(TAG_DATE);
                String orig = c.getString(TAG_ORIG);
                String dest = c.getString(TAG_DEST);
                String seats = c.getString(TAG_SEATS);

                if (depart != null && !depart.isEmpty() && !depart.equals("null"))
                    try {
                        Date time1 = new SimpleDateFormat("HH:mm").parse(depart);
                        Date time2 = new SimpleDateFormat("HH:mm").parse(time);
                        if (time1.after(time2))
                            continue;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                try {
                    int seats_needed = Integer.parseInt(needed_seats);
                    int seats_offered = Integer.parseInt(seats);
                    if(seats_needed > seats_offered)
                        continue;
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }
                // should use some more complex search algorithm based on Google Maps

                if (needed_date != null && !needed_date.isEmpty() && !needed_date.equals("null"))
                    try {
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(needed_date);
                        Date date2 = new SimpleDateFormat("HH:mm").parse(date);
                        if (!date1.equals(date2))
                            continue;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                if(!origin.toLowerCase().trim().equals(orig.toLowerCase().trim()) )
                    continue;

                // should use some more complex search algorithm based on Google Maps
                if(!destination.toLowerCase().trim().equals(dest.toLowerCase().trim()) )
                    continue;

                HashMap<String,String> trips = new HashMap<String,String>();
                trips.put(TAG_NAME,name);
                trips.put(TAG_UID,uid);
                trips.put(TAG_TIME, "In " + date + ", at "+time);
                trips.put(TAG_ORIG,"From "+orig);
                trips.put(TAG_DEST,"To "+dest);
                trips.put(TAG_SEATS,seats+" free seats");

                tripsList.add(trips);
            }

            ListAdapter adapter = new SimpleAdapter(
                    ShowSearchActivity.this, tripsList, R.layout.list_item,
                    new String[]{TAG_NAME, TAG_TIME, TAG_ORIG, TAG_DEST, TAG_SEATS},
                    new int[]{R.id.username, R.id.time, R.id.orig, R.id.dest, R.id.seats}
            );
            if (!tripsList.isEmpty())
                list.setAdapter(adapter);
            else
            {
                Toast.makeText(getApplicationContext(), "Sorry, no trips match your criteria" , Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(i);
                finish();
            }
            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
