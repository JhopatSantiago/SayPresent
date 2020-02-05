package com.example.saypresent.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database {
    public FirebaseDatabase ref = FirebaseDatabase.getInstance();
    public DatabaseReference organizerRef = ref.getReference("organizer");
    public DatabaseReference attendeeRef = ref.getReference("attendee");
}
