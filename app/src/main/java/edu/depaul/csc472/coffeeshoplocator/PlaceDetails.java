package edu.depaul.csc472.coffeeshoplocator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Marco on 11/20/16.
 */



public class PlaceDetails extends Activity{

    String name;
    String address;
    TextView name_view;
    TextView add_view;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cafe_details);
        Intent i = getIntent();
        if(i!=null) {
            name = i.getStringExtra("name");
            address = i.getStringExtra("address");
        }

        name_view = (TextView) findViewById(R.id.name);
        add_view = (TextView) findViewById(R.id.address);

        name_view.setText(name);
        add_view.setText(address);


    }

}