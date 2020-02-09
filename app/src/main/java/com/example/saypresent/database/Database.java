package com.example.saypresent.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    public FirebaseDatabase ref = FirebaseDatabase.getInstance();
    public DatabaseReference organizerRef = ref.getReference("organizer");
    public DatabaseReference attendeeRef = ref.getReference("attendee");
    public DatabaseReference eventRef = ref.getReference("event");
    public DatabaseReference checkpointRef = ref.getReference("event_checkpoint");
    public DatabaseReference attendanceRef = ref.getReference("attendance");
    public DatabaseReference event_attendeeRef = ref.getReference("event_attendee");
}
