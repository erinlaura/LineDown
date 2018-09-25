package com.engi5895.linedown.linedown;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;
import android.widget.TimePicker;

import java.sql.Time;

/**
 * Created by erinfitzgerald on 2018-03-23.
 */

public class AddManually extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_input_view);

        final Bundle bundle = getIntent().getExtras();

        final Context context = this;
        final Button submit_button = (Button) findViewById(R.id.submit_button_manual);

        submit_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                TimePicker startPicker = (TimePicker) findViewById(R.id.startPicker);
                TimePicker finishPicker = (TimePicker) findViewById(R.id.finishPicker);

                int startHour = startPicker.getCurrentHour();
                int finishHour = finishPicker.getCurrentHour();
                int startMinute = startPicker.getCurrentMinute();
                int finishMinute = finishPicker.getCurrentMinute();

                int waitTime = 60*finishHour + finishMinute - 60*startHour - startMinute;

                if (waitTime > 0) {
                    Log.d("Here", "erer");
                    Connection conn = new BackendConnection();
                    conn.addWaitTime(bundle.getString("restaurantID"), waitTime);
                }


            }
        });

    }
}

