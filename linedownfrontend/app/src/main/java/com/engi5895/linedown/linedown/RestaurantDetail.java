package com.engi5895.linedown.linedown;

import android.app.Activity;
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
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import android.content.Context;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Button;
import com.google.android.gms.maps.SupportMapFragment;
import android.app.Fragment;
import android.location.LocationManager;

/**
 * Created by erinfitzgerald on 2018-03-13.
 *
 */

public class RestaurantDetail extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_view);


        final Bundle bundle = getIntent().getExtras();

        TextView tt1 = (TextView) this.findViewById(R.id.restaurant_name);
        TextView tt2 = (TextView) this.findViewById(R.id.restaurant_waittime);
        TextView tt3 = (TextView) this.findViewById(R.id.restaurant_address);
        TextView tt4 = (TextView) this.findViewById(R.id.restaurant_distance);


        if (tt1 != null) {
            tt1.setText(bundle.getString("name"));
        }
        if (tt2 != null) {
            if ((bundle.getInt("wait_time") != 0)) {
                String placeholder = String.valueOf(bundle.getInt("wait_time"));
                tt2.setText("Wait Time : " + placeholder + " Mins");
            } else {
                String placeholder2 = "TBD";
                tt2.setText("Wait Time : " + placeholder2); //TODO: this produces some weird strings, fix that
            }
        }
        if (tt3 != null) {
            tt3.setText("Address : " + bundle.getString("address"));
        }

        final Context context = this;
        final Button button = (Button) findViewById(R.id.add_wait_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent detailIntent = new Intent(context, WaitMethodSelect.class);

                detailIntent.putExtra("name", (bundle.getString("name")));//restuarant_name might be just 'name'
                detailIntent.putExtra("wait_time", (bundle.getString("wait_time")));
                detailIntent.putExtra("address", (bundle.getString("address")));
                detailIntent.putExtra("restaurantID", (bundle.getString("restaurantID")));

                startActivity(detailIntent);
            }
        });
    }

}
