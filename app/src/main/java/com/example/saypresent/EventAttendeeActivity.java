package com.example.saypresent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * Displays all attendees of an Event
 * Event => Attendee
 */
public class EventAttendeeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendee);
    }
}
