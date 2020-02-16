package com.example.saypresent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saypresent.adapter.CheckpointsAdapter;
import com.example.saypresent.controller.EventCheckpointController;
import com.example.saypresent.controller.EventController;
import com.example.saypresent.model.Event;
import com.example.saypresent.model.EventCheckpoint;
import com.example.saypresent.utils.AddCheckpointInterface;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.GetEventCheckpoints;
import com.example.saypresent.utils.GetEventHandler;

import java.util.List;

/**
 * Displays Details of an Event
 * trigger for QR Scanner to ADD an ATTENDEE
 * trigger for Event's Registered ATTENDEES
 */
public class EventDetailActivity extends AppCompatActivity {

    private String organizer_key;
    private String event_key;

    private TextView event_name;
    private TextView event_location;
    private Button qr_button;
    private Button eventAttendeeBtn;
    private Button addCheckpointBtn;

    private EventController eventController = new EventController();
    private GetEventHandler getEventHandler;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<EventCheckpoint> eventCheckpoints;
    private EventCheckpointController eventCheckpointController = new EventCheckpointController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        final Intent intent = getIntent();
        organizer_key  = intent.getStringExtra("organizer_key");
        event_key = intent.getStringExtra("event_key");
        initialize();
        event_name = (TextView) findViewById(R.id.event_name);
        event_location = (TextView) findViewById(R.id.event_location);
        qr_button = (Button) findViewById(R.id.btn_qrcode);
        eventAttendeeBtn = (Button) findViewById(R.id.eventAttendeeBtn);
        addCheckpointBtn = (Button) findViewById(R.id.addCheckpointBtn);

        //this button will trigger qr_code scanner of an event
        qr_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent scannerIntent = new Intent(getApplicationContext(), QRScannerActivity.class);
                scannerIntent.putExtra("event_key", event_key);
                scannerIntent.putExtra("action", "event");
                startActivity(scannerIntent);
            }
        });

        //this button will trigger event attendee(EventAttendeeActivity.java) list activity
        eventAttendeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventAttendeeIntent = new Intent(getApplicationContext(), EventAttendeeActivity.class);
                eventAttendeeIntent.putExtra("event_key", event_key);
                startActivity(eventAttendeeIntent);
            }
        });

        addCheckpointBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent addCheckpointIntent = new Intent(getApplicationContext(), AddCheckpointActivity.class);
               addCheckpointIntent.putExtra("event_key", event_key);
               startActivity(addCheckpointIntent);
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.checkpointRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    protected void onResume() {
        super.onResume();
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

        eventCheckpointController.getEventCheckpoints(event_key, new GetEventCheckpoints() {
            @Override
            public void onGetCheckpoint(List<EventCheckpoint> eventCheckpoints) {
                if(eventCheckpoints != null){
                    instantiateAdapter(eventCheckpoints);
                }else{
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void instantiateAdapter(final List<EventCheckpoint> eventCheckpoints){
        CustomEventClickListener customEventClickListener = new CustomEventClickListener() {
            @Override
            public void onItemClick(View v, int i) {
                EventCheckpoint eventCheckpoint = eventCheckpoints.get(i);
                String checkpoint_key = eventCheckpoint.getCheckpoint_key();

                Intent intent = new Intent(getApplicationContext(), CheckpointActivity.class);
                intent.putExtra("checkpoint_key", checkpoint_key);
                intent.putExtra("event_key", event_key);
                startActivity(intent);
            }
        };

        mAdapter = new CheckpointsAdapter(eventCheckpoints, customEventClickListener);
        recyclerView.setAdapter(mAdapter);
    }
}
