package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.TextView;

import com.example.saypresent.controller.EventController;
import com.example.saypresent.model.Event;
import com.example.saypresent.utils.GetEventHandler;

public class EventDetailActivity extends AppCompatActivity {

    private String organizer_key;
    private String event_key;

    private TextView event_name;
    private TextView event_location;
    private Button qr_button;

    private EventController eventController = new EventController();
    private GetEventHandler getEventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Intent intent = getIntent();
        organizer_key  = intent.getStringExtra("organizer_key");
        event_key = intent.getStringExtra("event_key");

        event_name = (TextView) findViewById(R.id.event_name);
        event_location = (TextView) findViewById(R.id.event_location);
        qr_button = (Button) findViewById(R.id.btn_qrcode);

        //this button will trigger qr_code scanner of an event
        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scannerIntent = new Intent(getApplicationContext(), QRScannerActivity.class);
                scannerIntent.putExtra("organizer_key", organizer_key);
                scannerIntent.putExtra("event_key", event_key);
                startActivity(scannerIntent);
            }
        });

        initialize();
    }

    private void initialize(){
        getEventHandler = new GetEventHandler() {
            @Override
            public void onGetEvent(Event event) {
                String name = event.getEvent_name();
                String location = event.getLocation();

                event_name.setText(name);
                event_location.setText(location);
            }
        };
        eventController.getEvent(organizer_key, event_key, getEventHandler);
    }
}
