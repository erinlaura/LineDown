package com.engi5895.linedown.linedown;

/**
 * Created by jonathan on 04/03/18.
 */

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

import static com.engi5895.linedown.linedown.HttpRequest.CONNECTION_TIMEOUT;
import static com.engi5895.linedown.linedown.HttpRequest.READ_TIMEOUT;

public class BackendConnection implements Connection, HttpRequester {

    private static final String backendURL = "http://104.154.237.89/linedown";
    private final Restaurant restaurant;

    public BackendConnection() { this.restaurant = null; }

    public BackendConnection(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void getWaitTime(String ID) {

        String UrlString = backendURL + "/getwaittime?ID=" + ID;
        HttpRequest httpRequest = new HttpRequest();
        try {
            httpRequest.execute(UrlString, "GET", this);
        } catch (Exception e) {
            Log.d("error", e.getStackTrace().toString());
        }
    }

    @Override
    public void addWaitTime(String ID, int WaitTime) {
        String UrlString = backendURL + "/inputwaittime?ID=" + ID + "&WaitTime=" + WaitTime;
        Log.d("addWaiTime", UrlString);
        HttpRequest httpRequest = new HttpRequest();
        try {
            httpRequest.execute(UrlString, "GET", this);
        } catch (Exception e) {
        }
    }

    @Override
    public void nearbyRestaurant(String ID, boolean isNearby) {
        Log.d(ID, String.valueOf(isNearby));
        int nearby = 0;
        if (isNearby) {
            nearby = 1;
        }
        String urlString = backendURL + "/updateuserlocation?ID=" + ID + "&Nearby=" + String.valueOf(nearby);
        HttpRequest httpRequest = new HttpRequest();
        try {
            httpRequest.execute(urlString, "GET", this);
        } catch (Exception e) {
        }
    }

    @Override
    public void callback(String response) {
        if (!response.isEmpty()) {
            restaurant.setWaitTime(Integer.parseInt(response));
        }
        else {
        }
    }

}