package com.engi5895.linedown.linedown;

import android.util.Log;

import com.google.android.gms.location.places.*;


/**
 * Created by erinfitzgerald on 2018-03-04.
 *
 */

public class Restaurant implements Business {
    private String name;
    private String address;
    private Double lat;
    private Double lon;
    private String placeID;
    private int waitTime;
    private MainActivity mainActivity;


    public Restaurant(Place place) {
        this.name = String.valueOf(place.getName());
        this.address = String.valueOf(place.getAddress());
        this.lat = place.getLatLng().latitude;
        this.lon = place.getLatLng().longitude;
        this.placeID = place.getId();
        this.mainActivity = null;
        Connection conn = new BackendConnection(this);
        conn.getWaitTime(this.placeID);
    }

    public Restaurant (String Name, String Address, String Lat, String Lon, String PlaceID, int WaitTime) {
        this.name = Name;
        this.address = Address;
        this.lat = Double.valueOf(Lat);
        this.lon = Double.valueOf(Lon);
        this.waitTime = WaitTime; // Until true wait time received
        this.placeID = PlaceID;
        this.mainActivity = null;
    }


    public Restaurant (String Name, String Address, String Lat, String Lon, String PlaceID) {
        this.name = Name;
        this.address = Address;
        this.lat = Double.valueOf(Lat);
        this.lon = Double.valueOf(Lon);
        this.waitTime = 0; // Until true wait time received
        this.placeID = PlaceID;
        this.mainActivity = null;
        Connection conn = new BackendConnection(this);
        conn.getWaitTime(this.placeID);
    }

    public Restaurant (String Name, String Address, String Lat, String Lon, String PlaceID, MainActivity mainActivity) {
        this.name = Name;
        this.address = Address;
        this.lat = Double.valueOf(Lat);
        this.lon = Double.valueOf(Lon);
        this.waitTime = 0; // Until true wait time received
        this.placeID = PlaceID;
        this.mainActivity = mainActivity;
        Connection conn = new BackendConnection(this);
        conn.getWaitTime(this.placeID);
    }

    public void setWaitTime(int WaitTime) {
        this.waitTime = WaitTime;
        if (mainActivity != null) {
            mainActivity.onRestaurantWaitTimeReceived(this);
        }
        else {
            MainActivity.onSearchWaitTimeReceived(this);
        }
    }

    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public Double getLat(){
        return lat;
    }
    public Double getLon(){
        return lon;
    }
    public String getPlaceID(){
        return placeID;
    }
    public int getWaitTime(){
        return waitTime;
    }



}
