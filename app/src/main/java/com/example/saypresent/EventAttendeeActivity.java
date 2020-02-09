package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.saypresent.adapter.EventAttendeeAdapter;
import com.example.saypresent.controller.AttendeeController;
import com.example.saypresent.model.Attendee;
import com.example.saypresent.utils.CustomEventClickListener;
import com.example.saypresent.utils.GetEventAttendeeInterface;

import java.util.List;

/**
 * Displays all attendees of an Event
 * Event => Attendee
 */
public class EventAttendeeActivity extends AppCompatActivity {

    private String event_key;
    private AttendeeController attendeeController = new AttendeeController();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendee);

        Intent intent = getIntent();
        event_key = intent.getStringExtra("event_key");

        recyclerView = (RecyclerView) findViewById(R.id.eventAttendeeRecycler);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        attendeeController.getEventAtteendees(event_key, new GetEventAttendeeInterface() {
            @Override
            public void onGetEventAttendees(List<Attendee> attendees) {
                InstantiateAdapter(attendees);
            }
        });
    }

    private void InstantiateAdapter(final List<Attendee> attendees){
        mAdapter = new EventAttendeeAdapter(attendees, new CustomEventClickListener() {
            @Override
            public void onItemClick(View v, int i) {
                Log.i("attendee first name", attendees.get(i).getFirst_name());
            }
        });

        recyclerView.setAdapter(mAdapter);
    }
}
