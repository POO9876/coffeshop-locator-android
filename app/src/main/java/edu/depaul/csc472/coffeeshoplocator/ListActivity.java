package edu.depaul.csc472.coffeeshoplocator;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Marco on 11/21/16.
 */

public class ListActivity extends Activity {
    GooglePlaces googlePlaces;
    double longitude;
    double latitude;
    ListView lv;
    Button mapButton;
    List<Place> placesList;
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview);
        Intent i = getIntent();
        if (i != null) {
            longitude = i.getDoubleExtra("mainLong", -1);
            latitude = i.getDoubleExtra("mainLat", -1);
        }
        lv = (ListView) findViewById(R.id.list);
        new LoadList().execute();


        mapButton = (Button) findViewById(R.id.btn_show_map);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Place> pl = new ArrayList<>(placesList);
                Intent i = new Intent(ListActivity.this,CoffeeMapActivity.class);
                i.putParcelableArrayListExtra("placeList",pl);
                i.putExtra("list_long",longitude);
                i.putExtra("list_lat",latitude);
                startActivity(i);
            }
        });
    }

    class LoadList extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            googlePlaces = new GooglePlaces();
            try {
                placesList = googlePlaces.search(latitude, longitude, 1000, "cafe");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {

            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    if (true) {//if needed add STATUS condition
                        if (placesList != null) {
                            for (Place p : placesList) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put(KEY_REFERENCE, p.reference);

                                // Place name
                                map.put(KEY_NAME, p.name);


                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                            // list adapter
                            ListAdapter adapter = new SimpleAdapter(ListActivity.this, placesListItems,
                                    R.layout.list_item,
                                    new String[]{KEY_REFERENCE, KEY_NAME}, new int[]{
                                    R.id.reference, R.id.name});

                            // Adding data into listview
                            lv.setAdapter(adapter);

                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    final Intent i = new Intent(ListActivity.this,PlaceDetails.class);
                                    i.putExtra("name",placesList.get(position).name);
                                    i.putExtra("address",placesList.get(position).vicinity);
                                    startActivity(i);
                                }});
                        }
                    }
                }
            });
        }
    }
}
