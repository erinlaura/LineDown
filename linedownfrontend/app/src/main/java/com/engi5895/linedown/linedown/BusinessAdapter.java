package com.engi5895.linedown.linedown;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;

/**
 * Created by erinfitzgerald on 2018-03-04.
 *
 */

public class BusinessAdapter extends ArrayAdapter<Restaurant> {

    private List<Restaurant> restaurantList;
    private Context mContext;

    BusinessAdapter(Context context, List<Restaurant> r, MainActivity mainActivity){
        super(context, R.layout.restaurant_row, r);
        mContext = context;
        restaurantList = new ArrayList<>();
        for (int i = 0; i < r.size(); i++) {
            restaurantList.add(new Restaurant(r.get(i).getName(), r.get(i).getAddress(), r.get(i).getLat().toString(), r.get(i).getLon().toString(), r.get(i).getPlaceID(), mainActivity));
        }
    }
    private LayoutInflater tileInflater;

    //TODO add code to get the list of restaurants (from connection???)
    //or maybe not a TODO???

    @Override
    public int getCount(){
        if (restaurantList != null) {
            return restaurantList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Restaurant getItem(int position){
        return restaurantList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //TODO
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.restaurant_row, parent, false);
        }

        Restaurant r = getItem(position);


        if (r != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.restaurant_name);
            TextView tt2 = (TextView) v.findViewById(R.id.restaurant_waittime);
            TextView tt3 = (TextView) v.findViewById(R.id.restaurant_distance);

            if (tt1 != null) {
                tt1.setText(r.getName());
            }

            if (tt2 != null) {
                if (r.getWaitTime()!= 0) {
                    if(r.getWaitTime()==1){
                        tt2.setText(String.valueOf(r.getWaitTime())+ " Min");
                    }else if (r.getWaitTime()>=120){
                        tt2.setText("CLOSED");
                    }else {
                        tt2.setText(String.valueOf(r.getWaitTime()) + " Mins");
                    }
                }else{
                    tt2.setText(R.string.distance_placemarker);
                }
            }

            if (tt3 != null) {
                if(r.getLat()!=0 && r.getLon()!=0 && MainActivity.latLng!=null){

                    Double lat = MainActivity.latLng.latitude;
                    Double lon = MainActivity.latLng.longitude;
                    Float distance = getDistanceFromLatLonInKm(lat,lon,r.getLat(),r.getLon());
                    int ldistance = Math.round(distance);
                    if (ldistance > 1000){
                        DecimalFormat distancePrecise = new DecimalFormat("#.#");
                        distancePrecise.format(distance/1000);
                        tt3.setText(distancePrecise.format(distance/1000)+"km");
                    }else{
                        tt3.setText(String.valueOf(ldistance)+"m");
                    }

                }else {
                    tt3.setText(R.string.distance_placemarker);
                }
            }
        }

        return v;
    }

    public void updateRestaurantList(List<Restaurant> newList) {
        restaurantList.clear();
        restaurantList.addAll(newList);

        this.notifyDataSetChanged();
    }
    public Float getDistanceFromLatLonInKm(Double userLat,Double userLon,Double placeLat,Double placeLon) {

        Location loc1 = new Location("");
        loc1.setLatitude(userLat);
        loc1.setLongitude(userLon);

        Location loc2 = new Location("");
        loc2.setLatitude(placeLat);
        loc2.setLongitude(placeLon);

        return loc1.distanceTo(loc2);

    }
}

