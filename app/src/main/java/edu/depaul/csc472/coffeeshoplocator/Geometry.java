package edu.depaul.csc472.coffeeshoplocator;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marco on 12/6/16.
 */

public class Geometry implements Parcelable{

    public Location location;

    public Geometry(Location location) {
        this.location = location;
    }

    protected Geometry(Parcel in) {
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };

    @Override
    public String toString() {
        return "Geo: "+ location.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(location,-1);
    }
}
