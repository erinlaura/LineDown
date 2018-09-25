package com.engi5895.linedown.linedown;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.os.Build.ID;

/**
 * Created by jonathan on 04/03/18.
 */

public class PlaceAPIHandler implements HttpRequester {

    private final GeoDataClient mGdc;
    private List<String> placeIDs;
    private List<Restaurant> restaurants;
    private static MainActivity mainActivity;


    public PlaceAPIHandler (GeoDataClient gdc) {
        mGdc = gdc;
        placeIDs = new ArrayList<>();
        restaurants = new ArrayList<>();
        this.mainActivity = null;
    }

    public PlaceAPIHandler (GeoDataClient gdc, MainActivity mainActivity) {
        mGdc = gdc;
        placeIDs = new ArrayList<>();
       restaurants = new ArrayList<>();
       this.mainActivity = mainActivity;
    }

    public void getNearbyRestaurants (double searchRadius, LatLng location, String query) {
        Log.d("getNearbyRerants", "here");
        LatLngBounds bounds = toBounds(location, (searchRadius/1000));
        AutocompleteFilter filter = new AutocompleteFilter.Builder().setTypeFilter(AutocompleteFilter.TYPE_FILTER_ESTABLISHMENT).build();
        Task<AutocompletePredictionBufferResponse> results = mGdc.getAutocompletePredictions(query, bounds, filter);
        results.addOnSuccessListener(new OnSuccessListener<AutocompletePredictionBufferResponse>() {
            @Override
            public void onSuccess(AutocompletePredictionBufferResponse autocompletePredictions) {
                Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
                List<String> placeIDs = new ArrayList<>();
                while (iterator.hasNext()) {
                    AutocompletePrediction prediction = iterator.next();
                    placeIDs.add(prediction.getPlaceId());
                }
                PlaceAPIHandler.getPlacesFromIDs(placeIDs, mGdc);
                autocompletePredictions.release();
            }
        });
    }

    private LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

    private static void getPlacesFromIDs (final List<String> placeIDs, GeoDataClient gdc) {
        for (String placeID: placeIDs) {
            final Task<PlaceBufferResponse> place = gdc.getPlaceById(placeID);
            place.addOnSuccessListener(new OnSuccessListener<PlaceBufferResponse>() {
                @Override
                public void onSuccess(PlaceBufferResponse places) {
                    Iterator<Place> iterator = places.iterator();
                    List<Place> placeList = new ArrayList<>();
                    while (iterator.hasNext()) {
                        Place currentPlace = iterator.next();
                        if (currentPlace.getPlaceTypes().contains(Place.TYPE_MEAL_TAKEAWAY) || currentPlace.getPlaceTypes().contains(Place.TYPE_RESTAURANT)) {
                            placeList.add(currentPlace);
                        }
                    }
                    PlaceAPIHandler.setPlaces(placeList);
                    places.release();
                }
            });
        }
    }

    private static void setPlaces (List<Place> placeList) {
        MainActivity.onSearchResultsFound(placeList);
    }


    public void getRestaurants (double searchRadius, LatLng location) {
        HttpRequest httpRequest = new HttpRequest();
        String UrlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.latitude + "," + location.longitude + "&radius=" + searchRadius + "&type=meal_takeaway&key=" + MainActivity.API_key;
        try {
            httpRequest.execute(UrlString, "GET", this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", e.getStackTrace().toString());
        }
    }


    private void getPlacesFromIDsUsingBrowserAPI () {
        for (String placeID: placeIDs) {
            HttpRequest httpRequest = new HttpRequest();
            String UrlString = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeID + "&key=" + MainActivity.API_key;
            try {
                httpRequest.execute(UrlString, "GET", this);
            } catch (Exception e) {
            }
        }
    }


    @Override
    public void callback(String response) {
        if (placeIDs.size() == 0) {
            parseNearbyPlacesJson(response);
        }
        else {
            parsePlaceJson(response);
            if (restaurants.size() == placeIDs.size()) {
                if (mainActivity != null) {
                    mainActivity.onNearbyRestaurantsFound(restaurants);
                }
                placeIDs.clear();
                restaurants.clear();
            }
        }
    }


    private void parsePlaceJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject placeJson = (JSONObject) jsonObject.get("result");
            String name = placeJson.get("name").toString();
            JSONArray addressArray = (JSONArray) placeJson.get("address_components");
            JSONObject streetNumberJson = (JSONObject) addressArray.get(0);
            String streetNumber = streetNumberJson.get("short_name").toString();
            JSONObject streetNameJson = (JSONObject) addressArray.get(1);
            String streetName = streetNameJson.get("short_name").toString();
            String address = streetNumber + " " + streetName;
            JSONObject geometry = (JSONObject) placeJson.get("geometry");
            JSONObject locationJson = (JSONObject) geometry.get("location");
            String lat = locationJson.get("lat").toString();
            String lng = locationJson.get("lng").toString();
            String placeID = placeJson.get("place_id").toString();

            restaurants.add(new Restaurant(name, address, lat, lng, placeID, mainActivity));
        } catch (Exception e) {
        }
    }


    private void parseNearbyPlacesJson (String response) {
         try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject currentResult = (JSONObject) jsonArray.get(i);
                JSONArray placeTypes = (JSONArray) currentResult.get("types");
                for (int j = 0; j < placeTypes.length(); j++) {
                    if (placeTypes.get(j).toString().contains("restaurant") || placeTypes.get(j).toString().contains("meal_takeaway")) {
                        placeIDs.add(currentResult.get("place_id").toString());
                        Log.d("added", currentResult.get("place_id").toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", e.getStackTrace().toString());
        }
        getPlacesFromIDsUsingBrowserAPI();
    }

}
