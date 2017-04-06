package edu.depaul.csc472.coffeeshoplocator;

/**
 * Created by Marco on 11/21/16.
 */

import android.util.JsonReader;
import android.util.Log;



import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class GooglePlaces {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final String API_KEY = "AIzaSyBK2oxlADL5iF-1Dpe_fdAu5ZMVOf7xbW0";

    private static final String PLACES_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/search/json?";

    private double _latitude;
    private double _longitude;
    private double _radius;

    public List<Place> search(double latitude, double longitude, double radius, String types)
            throws Exception {
        this._latitude = latitude;
        this._longitude = longitude;
        this._radius = radius;
        List<Place> placeList;


        try {
            HttpRequestFactory httpRequestFactory = HTTP_TRANSPORT.createRequestFactory();
            HttpRequest request = httpRequestFactory
                    .buildGetRequest(new GenericUrl(PLACES_SEARCH_URL));
            request.getUrl().put("key", API_KEY);
            request.getUrl().put("location", _latitude + "," + _longitude);
            request.getUrl().put("radius", _radius); // in meters
            request.getUrl().put("sensor", "false");
            if (types != null)
                request.getUrl().put("types", types);
            HttpResponse r = request.execute();
            if(r.getContent()==null){
                Log.e("GOOGLE PLACES:", "NULL!!!!!");
                return null;
            }else{

                placeList = readJsonStream(r.getContent());
            }
        } catch (HttpResponseException e) {
            Log.e("Error:", e.getMessage());
            return null;
        }
        return placeList;

    }
    public List<Place> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<Place> placeList = null;
        try {
            reader.beginObject();
            while(reader.hasNext()){
                String n = reader.nextName();
                if(n.equals("html_attributions")){
                    reader.skipValue();
                }
                else if(n.equals("results")){
                    placeList = readPlaceArray(reader);
                }

                else{
                    reader.skipValue();
                }
            }

            return placeList;
        } finally {
            reader.close();
        }
    }
    public List<Place> readPlaceArray(JsonReader reader) throws IOException {
        List<Place> places = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            places.add(readPlace(reader));
        }
        reader.endArray();
        return places;
    }

    public Place readPlace(JsonReader reader) throws IOException {
        String id = null;
        String name = null;
        String reference = null;
        String icon = null;
        String vicinity = null;
        Geometry geometry = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String n = reader.nextName();
            if (n.equals("id")) {
                id = reader.nextString();
            } else if (n.equals("name")) {
                name = reader.nextString();
            } else if (n.equals("reference")) {
                reference = reader.nextString();
            } else if (n.equals("icon")) {
                icon = reader.nextString();
            } else if (n.equals("vicinity")){
                vicinity = reader.nextString();
            }
            else if (n.equals("geometry")){
                geometry = readGeometry(reader);
            }
            else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Place(id, name, reference, icon,vicinity,geometry);
    }

    public Geometry readGeometry(JsonReader reader) throws IOException {
        Location location = null;
        reader.beginObject();

        while (reader.hasNext()) {
            String n = reader.nextName();
            if(n.equals("location")){
                location = readLocation(reader);
            }
            else{
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Geometry(location);
    }

    public Location readLocation(JsonReader reader) throws IOException{
        double lat = 0;
        double lng = 0;

        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            if(name.equals("lat")){
                lat = reader.nextDouble();
            }else if(name.equals("lng")){
                lng = reader.nextDouble();
            }
        }
        reader.endObject();

        return new Location(lat,lng);
    }

}
