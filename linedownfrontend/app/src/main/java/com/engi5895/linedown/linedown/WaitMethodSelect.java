package com.engi5895.linedown.linedown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by erinfitzgerald on 2018-03-23.
 */

public class WaitMethodSelect extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_method_select);

        final Bundle bundle = getIntent().getExtras();

        final Context context = this;
        final Button manual_button = (Button) findViewById(R.id.manual_select);
        final Button timer_button = (Button) findViewById(R.id.timer_select);
        manual_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent detailIntent = new Intent(context, AddManually.class);

                detailIntent.putExtra("name", (bundle.getString("name")));//restuarant_name might be just 'name'
                detailIntent.putExtra("wait_time", (bundle.getString("wait_time")));
                detailIntent.putExtra("address", (bundle.getString("address")));
                detailIntent.putExtra("restaurantID", (bundle.getString("restaurantID")));

                startActivity(detailIntent);
            }
        });
        timer_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent detailIntent = new Intent(context, AddByTimer.class);

                detailIntent.putExtra("name", (bundle.getString("name")));//restuarant_name might be just 'name'
                detailIntent.putExtra("wait_time", (bundle.getString("wait_time")));
                detailIntent.putExtra("address", (bundle.getString("address")));
                detailIntent.putExtra("restaurantID", (bundle.getString("restaurantID")));

                startActivity(detailIntent);
            }
        });
    }
}


