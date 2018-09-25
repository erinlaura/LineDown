package com.engi5895.linedown.linedown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.os.SystemClock;

/**
 * Created by erinfitzgerald on 2018-03-23.
 */

public class AddByTimer extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer_view);

        final Bundle bundle = getIntent().getExtras();

        final Context context = this;

        //3 buttons for timer view
        final Button submit_button = (Button) findViewById(R.id.submit_button_timer);
        final Button start_button = (Button) findViewById(R.id.start_button);
        final Button stop_button = (Button) findViewById(R.id.stop_button);
        final Button restart_button = (Button) findViewById(R.id.restart_button);

        //Chronometer for timer view
        final Chronometer timerTime = (Chronometer) findViewById(R.id.timer_time);

        //button onClick Listeners
        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("Time", String.valueOf(SystemClock.elapsedRealtime()-timerTime.getBase()));
                Connection conn = new BackendConnection();
                int waitTime = (int) ( (SystemClock.elapsedRealtime()-timerTime.getBase()) / 60000);
                conn.addWaitTime(bundle.getString("restaurantID"), waitTime);
            }
        });
        start_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                timerTime.setBase(SystemClock.elapsedRealtime());
                timerTime.start();
            }
        });
        stop_button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                timerTime.stop();
            }
        });
        restart_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                timerTime.stop();
                timerTime.setBase(SystemClock.elapsedRealtime());
            }
        });

    }
}
