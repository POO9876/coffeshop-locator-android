package edu.depaul.csc472.coffeeshoplocator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marco on 12/6/16.
 */

public class Location implements Parcelable{

    public double lat;
    public double lng;

    public Location(double lat, double lng) {
        this.lng = lng;
        this.lat = lat;
    }

    protected Location(Parcel in) {
        lat = in.readDouble();
        lng = in.readDouble();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public String toString() {
        return "Lat: " + lat + "Lng: "+ lng;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(lat);
        dest.writeDouble(lng);
    }
}
