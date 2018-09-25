package com.engi5895.linedown.linedown;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.os.StrictMode;
import android.util.Log;
import android.Manifest;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ArrayAdapter;
//import android.content.Intent;
import android.content.Context;
import android.content.Intent;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private static FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    public static List<Restaurant> restaurants = new ArrayList<>();
    private List<String> PlaceIDs = new ArrayList<>();
    private List<String> nearbyRestaurants = new ArrayList<>();
    private static BusinessAdapter nearbyRestaurantAdapter;
    //private ArrayAdapter adapter;
    public static final String API_key = "AIzaSyC1gT1axMrmM2vniWX3jibLiq0Sv0fYBVc";
    private static SearchView searchView;
    public static LatLng latLng = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView nearbyRestaurantList = findViewById(R.id.nearbyRestaurantList);

        searchView = findViewById(R.id.search_view);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                PlaceAPIHandler placeAPIHandler = new PlaceAPIHandler(mGeoDataClient);
                placeAPIHandler.getNearbyRestaurants(2000, latLng, s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        nearbyRestaurantAdapter = new BusinessAdapter(this, restaurants, this);
        nearbyRestaurantList.setAdapter(nearbyRestaurantAdapter);

        final Context context = this;
        nearbyRestaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Restaurant selectedRestaurant = restaurants.get(position);

                Intent detailIntent = new Intent(context, RestaurantDetail.class);

                detailIntent.putExtra("name", selectedRestaurant.getName());
                detailIntent.putExtra("wait_time",selectedRestaurant.getWaitTime());
                detailIntent.putExtra("address",selectedRestaurant.getAddress());
                detailIntent.putExtra("restaurantID",selectedRestaurant.getPlaceID());

                startActivity(detailIntent);
            }

        });



        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        startLocationUpdates();
    }

    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(20000);
        mLocationRequest.setFastestInterval(12000);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

           LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {
                        onLocationChanged(location);
                    }
                }
            },
            Looper.myLooper());
        }
    }

    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.d("Location", "changed");
        Log.d(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude));

        PlaceAPIHandler pAPI = new PlaceAPIHandler(mGeoDataClient, this);

        pAPI.getRestaurants(2000.0, latLng);

        checkIfAtRestaurant();

    }

    public void onNearbyRestaurantsFound(List<Restaurant> restaurantList) {
        restaurants.clear();
        List<String> usedPlaceIDs = new ArrayList<>();
        for (Restaurant r : restaurantList) {
            if (!usedPlaceIDs.contains(r.getPlaceID())) {
                restaurants.add(new Restaurant(r.getName(), r.getAddress(), r.getLat().toString(), r.getLon().toString(), r.getPlaceID(), this));
            }
            usedPlaceIDs.add(r.getPlaceID());
        }

        List<Restaurant> anotherRestaurantList = new ArrayList<>();
        for (int i = 0; i < restaurants.size(); i++) {
            anotherRestaurantList.add(new Restaurant(restaurants.get(i).getName(), restaurants.get(i).getAddress(), restaurants.get(i).getLat().toString(), restaurants.get(i).getLon().toString(), restaurants.get(i).getPlaceID(), this));
        }
        nearbyRestaurantAdapter.updateRestaurantList(anotherRestaurantList);
    }

    public static void onSearchResultsFound(List<Place> restaurantList) {
        restaurants.clear();
        List<String> usedPlaceIDs = new ArrayList<>();
        for (Place r : restaurantList) {
            if (!usedPlaceIDs.contains(r.getId())) {
                restaurants.add(new Restaurant(r));
            }
            usedPlaceIDs.add(r.getId());
        }
        List<Restaurant> anotherRestaurantList = new ArrayList<>();
        for (int i = 0; i < restaurants.size(); i++) {
            anotherRestaurantList.add(new Restaurant(restaurants.get(i).getName(), restaurants.get(i).getAddress(), restaurants.get(i).getLat().toString(), restaurants.get(i).getLon().toString(), restaurants.get(i).getPlaceID()));
        }
        nearbyRestaurantAdapter.updateRestaurantList(anotherRestaurantList);
    }


    public void onRestaurantWaitTimeReceived(Restaurant updatedRestaurant) {
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant currentRestaurant = restaurants.get(i);
            if (currentRestaurant.getPlaceID() == updatedRestaurant.getPlaceID()) {
                restaurants.set(i, updatedRestaurant);
                break;
            }
        }
        List<Restaurant> anotherRestaurantList = new ArrayList<>();
        for (int i = 0; i < restaurants.size(); i++) {
            anotherRestaurantList.add(new Restaurant(restaurants.get(i).getName(), restaurants.get(i).getAddress(), restaurants.get(i).getLat().toString(), restaurants.get(i).getLon().toString(), restaurants.get(i).getPlaceID(), restaurants.get(i).getWaitTime()));
        }
        Collections.sort(restaurants, new Comparator<Restaurant>() {
            @Override
            public int compare(Restaurant restaurant, Restaurant t1) {
                return restaurant.getWaitTime()-t1.getWaitTime();
            }
        });
        nearbyRestaurantAdapter.updateRestaurantList(restaurants);
    }

    public static void onSearchWaitTimeReceived(Restaurant updatedRestaurant) {
        Log.d("onSer", "jkkjk:");
        for (int i = 0; i < restaurants.size(); i++) {
            Restaurant currentRestaurant = restaurants.get(i);
            if (currentRestaurant.getPlaceID() == updatedRestaurant.getPlaceID()) {
                restaurants.set(i, updatedRestaurant);
                break;
            }
        }
        List<Restaurant> anotherRestaurantList = new ArrayList<>();
        for (int i = 0; i < restaurants.size(); i++) {
            anotherRestaurantList.add(new Restaurant(restaurants.get(i).getName(), restaurants.get(i).getAddress(), restaurants.get(i).getLat().toString(), restaurants.get(i).getLon().toString(), restaurants.get(i).getPlaceID(), restaurants.get(i).getWaitTime()));
        }

        nearbyRestaurantAdapter.updateRestaurantList(restaurants);
    }

    private synchronized void checkIfAtRestaurant() {
        PlaceIDs.clear();
        for (Restaurant r: restaurants) {
            PlaceIDs.add(r.getPlaceID());
        }

        PlaceFilter restaurantFilter = new PlaceFilter(false, PlaceIDs);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            final Task<PlaceLikelihoodBufferResponse> nearbyPlaces = mPlaceDetectionClient.getCurrentPlace(restaurantFilter);
            nearbyPlaces.addOnSuccessListener(new OnSuccessListener<PlaceLikelihoodBufferResponse>() {
                @Override
                public synchronized void onSuccess(PlaceLikelihoodBufferResponse placeLikelihoods) {
                    Iterator<PlaceLikelihood> iterator = placeLikelihoods.iterator();
                    List<String> nearbyIDs = new ArrayList<>();
                    Connection conn = new BackendConnection();
                    while (iterator.hasNext()) {
                        PlaceLikelihood next = iterator.next();
                        if (next.getLikelihood() > 0.5) {
                            nearbyIDs.add(next.getPlace().getId());
                        }
                    }
                    for (String ID: nearbyIDs) {
                        if (!nearbyRestaurants.contains(ID)) {
                            conn.nearbyRestaurant(ID, true);
                            nearbyRestaurants.add(ID);
                        }
                    }
                    List<String> IDsToRemove = new ArrayList<>();
                    for (String ID: nearbyRestaurants) {
                        if (!nearbyIDs.contains(ID)) {
                            conn.nearbyRestaurant(ID, false);
                            for (int i = 0; i < nearbyRestaurants.size(); i++) {
                                if (nearbyRestaurants.get(i) == ID) {
                                    IDsToRemove.add(ID);
                                }
                            }
                        }
                    }
                    Iterator<String> iter = nearbyRestaurants.iterator();
                    while (iter.hasNext()) {
                        String id = iter.next();
                        if (IDsToRemove.contains(id)) {
                            iter.remove();
                        }
                    }
                    placeLikelihoods.release();
                }
            });
        }

    }


}