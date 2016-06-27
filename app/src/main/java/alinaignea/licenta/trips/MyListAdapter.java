package alinaignea.licenta.trips;

import android.content.ClipData;
import android.content.Context;
import android.provider.Browser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import alinaignea.licenta.R;

/**
 * Created by Alina Ignea on 6/23/2016.
 */
class MyListAdapter extends SimpleAdapter {

    private ArrayList<HashMap<String, String>> c;
    private Context context;


    public MyListAdapter(Context context, int layout, ArrayList c, String[] from,
                         int[] to) {
        super(context, c, layout, from, to);
        // TODO Auto-generated constructor stub
        this.c = c;
        this.context = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater vi;
        vi = LayoutInflater.from(context);
        v = vi.inflate(R.layout.list_item, null);
        return v;
    }

}