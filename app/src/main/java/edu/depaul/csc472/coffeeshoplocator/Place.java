package edu.depaul.csc472.coffeeshoplocator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marco on 11/20/16.
 */


public class Place implements Parcelable {

    String id;
    String name;
    String reference;
    String icon;
    String vicinity;
    Geometry geometry;

    public Place(String id, String name, String icon, String reference, String vicinity, Geometry geometry) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.reference = reference;
        this.vicinity = vicinity;
        this.geometry = geometry;
    }


    protected Place(Parcel in) {
        id = in.readString();
        name = in.readString();
        reference = in.readString();
        icon = in.readString();
        vicinity = in.readString();
        geometry = in.readParcelable(Geometry.class.getClassLoader());
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public String toString() {
        return name + ": " + vicinity;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(reference);
        dest.writeString(icon);
        dest.writeString(vicinity);
        dest.writeParcelable(geometry,-1);
    }
}
